package com.roncoo.eshop.cache.hystrix.command;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.spring.SpringContext;

public class SaveShopInfo2ReidsCacheCommand extends HystrixCommand<Boolean> {

	private ShopInfo shopInfo;
	
	public SaveShopInfo2ReidsCacheCommand(ShopInfo shopInfo) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(100)
						.withCircuitBreakerRequestVolumeThreshold(1000)
						.withCircuitBreakerErrorThresholdPercentage(70)
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000))
				);  
		this.shopInfo = shopInfo;
	}
	
	@Override
	protected Boolean run() throws Exception {
		JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext()
				.getBean("JedisClusterFactory"); 
		String key = "shop_info_" + shopInfo.getId();
		jedisCluster.set(key, JSONObject.toJSONString(shopInfo));  
		return true;
	}
	
	@Override
	protected Boolean getFallback() {
		return false;
	}

}
