package com.roncoo.eshop.cache.ha.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class UpdateProductInfoCommand extends HystrixCommand<Boolean> {

	@SuppressWarnings("unused")
	private Long productId;
	
	public UpdateProductInfoCommand(Long productId) {
		super(HystrixCommandGroupKey.Factory.asKey("UpdateProductInfoGroup"));
		this.productId = productId;
	}
	
	@Override
	protected Boolean run() throws Exception {
		// 执行一次商品信息的更新
//		GetProductInfoCommand.flushCache(productId);  
		return true;
	}  

}
