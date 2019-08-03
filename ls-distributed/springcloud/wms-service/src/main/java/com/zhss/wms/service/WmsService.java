package com.zhss.wms.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zhss.wms_api.WmsApi;

@RestController
public class WmsService implements WmsApi {

	public String delivery(@PathVariable("productId") Long productId) {
		System.out.println("对商品【productId=" + productId + "】进行发货");    
		return "{'msg': 'success'}";
	}
	
}