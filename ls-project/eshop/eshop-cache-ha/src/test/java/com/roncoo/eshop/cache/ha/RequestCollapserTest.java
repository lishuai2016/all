package com.roncoo.eshop.cache.ha;

import com.roncoo.eshop.cache.ha.http.HttpClientUtils;

public class RequestCollapserTest {
	
	public static void main(String[] args) throws Exception {
		HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfos?productIds=1,1,2,2,3,4"); 
	}
	
}
