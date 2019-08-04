package com.roncoo.eshop.cache.ha.hystrix.command;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import com.roncoo.eshop.cache.ha.http.HttpClientUtils;
import com.roncoo.eshop.cache.ha.model.ProductInfo;

/**
 * 批量查询多个商品数据的command
 * @author Administrator
 *
 */
public class GetProductInfosCommand extends HystrixObservableCommand<ProductInfo> {

	private String[] productIds;
	
	public GetProductInfosCommand(String[] productIds) {
		super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
		this.productIds = productIds;
	}
	
	@Override
	protected Observable<ProductInfo> construct() {
		return Observable.create(new Observable.OnSubscribe<ProductInfo>() {

			public void call(Subscriber<? super ProductInfo> observer) {
				try {
					for(String productId : productIds) {
						String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
						String response = HttpClientUtils.sendGetRequest(url);
						ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class); 
						observer.onNext(productInfo); 
					}
					observer.onCompleted();
				} catch (Exception e) {
					observer.onError(e);  
				}
			}
			
		}).subscribeOn(Schedulers.io());
	}

}
