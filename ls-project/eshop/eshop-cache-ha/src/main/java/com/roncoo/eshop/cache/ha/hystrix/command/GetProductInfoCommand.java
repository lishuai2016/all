package com.roncoo.eshop.cache.ha.hystrix.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.roncoo.eshop.cache.ha.cache.local.BrandCache;
import com.roncoo.eshop.cache.ha.cache.local.LocationCache;
import com.roncoo.eshop.cache.ha.http.HttpClientUtils;
import com.roncoo.eshop.cache.ha.model.ProductInfo;

/**
 * 获取商品信息
 * @author Administrator
 *
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {

	public static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("GetProductInfoCommand");
	
	private Long productId;
	
	public GetProductInfoCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
				.andCommandKey(KEY)
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetProductInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(3)
						.withMaximumSize(30) 
						.withAllowMaximumSizeToDivergeFromCoreSize(true) 
						.withKeepAliveTimeMinutes(1) 
						.withMaxQueueSize(12)
						.withQueueSizeRejectionThreshold(15)) 
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerRequestVolumeThreshold(30)
						.withCircuitBreakerErrorThresholdPercentage(40)
						.withCircuitBreakerSleepWindowInMilliseconds(3000)
						.withExecutionTimeoutInMilliseconds(500)
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(30))
				);  
//		super(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"));
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
//		System.out.println("调用接口，查询商品数据，productId=" + productId); 
//		
		if(productId.equals(-1L)) {
			throw new Exception();
		}
		if(productId.equals(-2L)) {
			throw new Exception();
		}
//		
//		if(productId.equals(-2L)) {
//			Thread.sleep(3000);  
//		}
//		
//		if(productId.equals(-3L)) {
////			Thread.sleep(250); 
//		}
		
		String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, ProductInfo.class);  
	}
	
//	@Override
//	protected String getCacheKey() {
//		return "product_info_" + productId;
//	}
	
	@Override
	protected ProductInfo getFallback() {
		return new FirstLevelFallbackCommand(productId).execute();
	}
	
	private static class FirstLevelFallbackCommand extends HystrixCommand<ProductInfo> {

		private Long productId;
		
		public FirstLevelFallbackCommand(Long productId) {
			// 第一级的降级策略，因为这个command是运行在fallback中的
			// 所以至关重要的一点是，在做多级降级的时候，要将降级command的线程池单独做一个出来
			// 如果主流程的command都失败了，可能线程池都已经被占满了
			// 降级command必须用自己的独立的线程池
			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
						.andCommandKey(HystrixCommandKey.Factory.asKey("FirstLevelFallbackCommand"))
						.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstLevelFallbackPool"))
			);  
			this.productId = productId;
		}
		
		@Override
		protected ProductInfo run() throws Exception {
			// 这里，因为是第一级降级的策略，所以说呢，其实是要从备用机房的机器去调用接口
			// 但是，我们这里没有所谓的备用机房，所以说还是调用同一个服务来模拟
			if(productId.equals(-2L)) {
				throw new Exception();
			}
			String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
			String response = HttpClientUtils.sendGetRequest(url);
			return JSONObject.parseObject(response, ProductInfo.class);  
		}
		
		@Override
		protected ProductInfo getFallback() {
			// 第二级降级策略，第一级降级策略，都失败了
			ProductInfo productInfo = new ProductInfo();
			// 从请求参数中获取到的唯一条数据
			productInfo.setId(productId); 
			// 从本地缓存中获取一些数据
			productInfo.setBrandId(BrandCache.getBrandId(productId));
			productInfo.setBrandName(BrandCache.getBrandName(productInfo.getBrandId()));  
			productInfo.setCityId(LocationCache.getCityId(productId)); 
			productInfo.setCityName(LocationCache.getCityName(productInfo.getCityId()));  
			// 手动填充一些默认的数据
			productInfo.setColor("默认颜色");  
			productInfo.setModifiedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			productInfo.setName("默认商品");  
			productInfo.setPictureList("default.jpg");  
			productInfo.setPrice(0.0);  
			productInfo.setService("默认售后服务");  
			productInfo.setShopId(-1L);  
			productInfo.setSize("默认大小");  
			productInfo.setSpecification("默认规格");   
			return productInfo;
		}
		
	}
	
//	public static void flushCache(Long productId) {
//		HystrixRequestCache.getInstance(KEY,
//                HystrixConcurrencyStrategyDefault.getInstance()).clear("product_info_" + productId);
//	}
	
}
