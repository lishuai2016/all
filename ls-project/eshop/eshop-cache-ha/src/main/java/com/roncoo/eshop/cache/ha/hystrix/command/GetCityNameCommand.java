package com.roncoo.eshop.cache.ha.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.roncoo.eshop.cache.ha.cache.local.LocationCache;

/**
 * 获取城市名称的command
 * @author Administrator
 *
 */
public class GetCityNameCommand extends HystrixCommand<String> {

	private Long cityId;
	
	public GetCityNameCommand(Long cityId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetCityNameGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetCityNameCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetCityNamePool"))
		        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		        		.withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE)
		        		.withExecutionIsolationSemaphoreMaxConcurrentRequests(15)));
		this.cityId = cityId;
	}
	
	@Override
	protected String run() throws Exception {
		return LocationCache.getCityName(cityId);
	}
	
}
