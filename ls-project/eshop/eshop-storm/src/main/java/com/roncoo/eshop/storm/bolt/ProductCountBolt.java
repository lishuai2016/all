package com.roncoo.eshop.storm.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.storm.shade.org.json.simple.JSONArray;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roncoo.eshop.storm.http.HttpClientUtils;
import com.roncoo.eshop.storm.zk.ZooKeeperSession;

/**
 * 商品访问次数统计bolt
 * @author Administrator
 *
 */
public class ProductCountBolt extends BaseRichBolt {

	private static final long serialVersionUID = -8761807561458126413L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCountBolt.class);

	private LRUMap<Long, Long> productCountMap = new LRUMap<Long, Long>(1000);
	private ZooKeeperSession zkSession;
	private int taskid;
	
	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.zkSession = ZooKeeperSession.getInstance();
		this.taskid = context.getThisTaskId();
		
		new Thread(new ProductCountThread()).start();
		new Thread(new HotProductFindThread()).start();
		
		// 1、将自己的taskid写入一个zookeeper node中，形成taskid的列表
		// 2、然后每次都将自己的热门商品列表，写入自己的taskid对应的zookeeper节点
		// 3、然后这样的话，并行的预热程序才能从第一步中知道，有哪些taskid
		// 4、然后并行预热程序根据每个taskid去获取一个锁，然后再从对应的znode中拿到热门商品列表
		initTaskId(context.getThisTaskId());
	}
	
	private void initTaskId(int taskid) {
		// ProductCountBolt所有的task启动的时候， 都会将自己的taskid写到同一个node的值中
		// 格式就是逗号分隔，拼接成一个列表
		// 111,211,355
		
		zkSession.acquireDistributedLock();
		
		zkSession.createNode("/taskid-list");  
		String taskidList = zkSession.getNodeData();
		LOGGER.info("【ProductCountBolt获取到taskid list】taskidList=" + taskidList);  
		if(!"".equals(taskidList)) {
			taskidList += "," + taskid;
		} else {
			taskidList += taskid;
		}
		
		zkSession.setNodeData("/taskid-list", taskidList);  
		LOGGER.info("【ProductCountBolt设置taskid list】taskidList=" + taskidList);  
		
		zkSession.releaseDistributedLock();
	}
	
	private class HotProductFindThread implements Runnable {

		@SuppressWarnings("deprecation")
		public void run() {
			List<Map.Entry<Long, Long>> productCountList = new ArrayList<Map.Entry<Long, Long>>();
			List<Long> hotProductIdList = new ArrayList<Long>();
			List<Long> lastTimeHotProductIdList = new ArrayList<Long>();
			
			while(true) {
				// 1、将LRUMap中的数据按照访问次数，进行全局的排序
				// 2、计算95%的商品的访问次数的平均值
				// 3、遍历排序后的商品访问次数，从最大的开始
				// 4、如果某个商品比如它的访问量是平均值的10倍，就认为是缓存的热点
				try {
					productCountList.clear();
					hotProductIdList.clear();
					
					if(productCountMap.size() == 0) {
						Utils.sleep(100);
						continue;
					}
					
					LOGGER.info("【HotProductFindThread打印productCountMap的长度】size=" + productCountMap.size());
					
					// 1、先做全局的排序
					
					for(Map.Entry<Long, Long> productCountEntry : productCountMap.entrySet()) {
						if(productCountList.size() == 0) {
							productCountList.add(productCountEntry);
						} else {
							// 比较大小，生成最热topn的算法有很多种
							// 但是我这里为了简化起见，不想引入过多的数据结构和算法的的东西
							// 很有可能还是会有漏洞，但是我已经反复推演了一下了，而且也画图分析过这个算法的运行流程了
							boolean bigger = false;
							
							for(int i = 0; i < productCountList.size(); i++){
								Map.Entry<Long, Long> topnProductCountEntry = productCountList.get(i);
								
								if(productCountEntry.getValue() > topnProductCountEntry.getValue()) {
									int lastIndex = productCountList.size() < productCountMap.size() ? productCountList.size() - 1 : productCountMap.size() - 2;
									for(int j = lastIndex; j >= i; j--) {
										if(j + 1 == productCountList.size()) {
											productCountList.add(null);
										}
										productCountList.set(j + 1, productCountList.get(j));  
									}
									productCountList.set(i, productCountEntry);
									bigger = true;
									break;
								}
							}
							
							if(!bigger) {
								if(productCountList.size() < productCountMap.size()) {
									productCountList.add(productCountEntry);
								}
							}
						}
					}
					
					LOGGER.info("【HotProductFindThread全局排序后的结果】productCountList=" + productCountList); 
					
					// 2、计算出95%的商品的访问次数的平均值
					int calculateCount = (int)Math.floor(productCountList.size() * 0.95);
					
					Long totalCount = 0L;
					for(int i = productCountList.size() - 1; i >= productCountList.size() - calculateCount; i--) {
						totalCount += productCountList.get(i).getValue();
					}
					
					Long avgCount = totalCount / calculateCount;
					
					LOGGER.info("【HotProductFindThread计算出95%的商品的访问次数平均值】avgCount=" + avgCount); 
					
					// 3、从第一个元素开始遍历，判断是否是平均值得10倍
					for(Map.Entry<Long, Long> productCountEntry : productCountList) {
						if(productCountEntry.getValue() > 10 * avgCount) {
							LOGGER.info("【HotProductFindThread发现一个热点】productCountEntry=" + productCountEntry); 
							hotProductIdList.add(productCountEntry.getKey());
							
							if(!lastTimeHotProductIdList.contains(productCountEntry.getKey())) {
								// 将缓存热点反向推送到流量分发的nginx中
								String distributeNginxURL = "http://192.168.31.227/hot?productId=" + productCountEntry.getKey();
								HttpClientUtils.sendGetRequest(distributeNginxURL);
								
								// 将缓存热点，那个商品对应的完整的缓存数据，发送请求到缓存服务去获取，反向推送到所有的后端应用nginx服务器上去
								String cacheServiceURL = "http://192.168.31.179:8080/getProductInfo?productId=" + productCountEntry.getKey();
								String response = HttpClientUtils.sendGetRequest(cacheServiceURL);
								
								List<NameValuePair> params = new ArrayList<NameValuePair>();  
								params.add(new BasicNameValuePair("productInfo", response));    
						        String productInfo = URLEncodedUtils.format(params, HTTP.UTF_8);
								
								String[] appNginxURLs = new String[]{
										"http://192.168.31.187/hot?productId=" + productCountEntry.getKey() + "&" + productInfo,
										"http://192.168.31.19/hot?productId=" + productCountEntry.getKey() + "&" + productInfo
								};
								
								for(String appNginxURL : appNginxURLs) {
									HttpClientUtils.sendGetRequest(appNginxURL);
								}
							}
						}
					}
					
					// 4、实时感知热点数据的消失
					if(lastTimeHotProductIdList.size() == 0) {
						if(hotProductIdList.size() > 0) {
							for(Long productId : hotProductIdList) {
								lastTimeHotProductIdList.add(productId);
							}
							LOGGER.info("【HotProductFindThread保存上次热点数据】lastTimeHotProductIdList=" + lastTimeHotProductIdList);
						}
					} else {
						for(Long productId : lastTimeHotProductIdList) {
							if(!hotProductIdList.contains(productId)) {
								LOGGER.info("【HotProductFindThread发现一个热点消失了】productId=" + productId); 
								// 说明上次的那个商品id的热点，消失了
								// 发送一个http请求给到流量分发的nginx中，取消热点缓存的标识
								String url = "http://192.168.31.227/cancel_hot?productId=" + productId;
								HttpClientUtils.sendGetRequest(url);
							}
						}
						
						if(hotProductIdList.size() > 0) {
							lastTimeHotProductIdList.clear();
							for(Long productId : hotProductIdList) {
								lastTimeHotProductIdList.add(productId);
							}
							LOGGER.info("【HotProductFindThread保存上次热点数据】lastTimeHotProductIdList=" + lastTimeHotProductIdList);
						} else {
							lastTimeHotProductIdList.clear();
						}
					}
					
					Utils.sleep(5000); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class ProductCountThread implements Runnable {
		
		public void run() {
			List<Map.Entry<Long, Long>> topnProductList = new ArrayList<Map.Entry<Long, Long>>();   
			List<Long> productidList = new ArrayList<Long>();
			
			while(true) {
				try {
					topnProductList.clear();
					productidList.clear();
					
					int topn = 3;
					
					if(productCountMap.size() == 0) {
						Utils.sleep(100);
						continue;
					}
					
					LOGGER.info("【ProductCountThread打印productCountMap的长度】size=" + productCountMap.size());
					
					for(Map.Entry<Long, Long> productCountEntry : productCountMap.entrySet()) {
						if(topnProductList.size() == 0) {
							topnProductList.add(productCountEntry);
						} else {
							// 比较大小，生成最热topn的算法有很多种
							// 但是我这里为了简化起见，不想引入过多的数据结构和算法的的东西
							// 很有可能还是会有漏洞，但是我已经反复推演了一下了，而且也画图分析过这个算法的运行流程了
							boolean bigger = false;
							
							for(int i = 0; i < topnProductList.size(); i++){
								Map.Entry<Long, Long> topnProductCountEntry = topnProductList.get(i);
								
								if(productCountEntry.getValue() > topnProductCountEntry.getValue()) {
									int lastIndex = topnProductList.size() < topn ? topnProductList.size() - 1 : topn - 2;
									for(int j = lastIndex; j >= i; j--) {
										if(j + 1 == topnProductList.size()) {
											topnProductList.add(null);
										}
										topnProductList.set(j + 1, topnProductList.get(j));  
									}
									topnProductList.set(i, productCountEntry);
									bigger = true;
									break;
								}
							}
							
							if(!bigger) {
								if(topnProductList.size() < topn) {
									topnProductList.add(productCountEntry);
								}
							}
						}
					}
					
					// 获取到一个topn list
					for(Map.Entry<Long, Long> topnProductEntry : topnProductList) {
						productidList.add(topnProductEntry.getKey());
					}
					
					String topnProductListJSON = JSONArray.toJSONString(productidList);
					zkSession.createNode("/task-hot-product-list-" + taskid); 
					zkSession.setNodeData("/task-hot-product-list-" + taskid, topnProductListJSON);
					LOGGER.info("【ProductCountThread计算出一份top3热门商品列表】zk path=" + ("/task-hot-product-list-" + taskid) + ", topnProductListJSON=" + topnProductListJSON);  
					
					Utils.sleep(5000); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void execute(Tuple tuple) {
		Long productId = tuple.getLongByField("productId"); 
		
		LOGGER.info("【ProductCountBolt接收到一个商品id】 productId=" + productId);  
		
		Long count = productCountMap.get(productId);
		if(count == null) {
			count = 0L;
		}
		count++;
		
		productCountMap.put(productId, count);
		
		LOGGER.info("【ProductCountBolt完成商品访问次数统计】productId=" + productId + ", count=" + count); 
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
