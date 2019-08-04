package com.roncoo.eshop.cache.ha.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.roncoo.eshop.cache.ha.model.ProductInfo;

public class GetProductInfoFromMySQLCommand extends HystrixCommand<ProductInfo> {

	private Long productId;
	
	public GetProductInfoFromMySQLCommand(Long productId) {
		super(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"));
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		// 模拟从mysql中直接去查询，获取一些数据，但是因为我们不是太了解业务逻辑，所以只能取到少数一些字段
		String json = "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"modifiedTime\": \"2017-01-01 12:00:00\"}";
		return JSONObject.parseObject(json, ProductInfo.class); 
	} 
	
}
