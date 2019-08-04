package com.roncoo.eshop.cache.ha.cache.local;

import java.util.HashMap;
import java.util.Map;

public class LocationCache {

	private static Map<Long, String> cityMap = new HashMap<Long, String>();
	private static Map<Long, Long> productCityMap = new HashMap<Long, Long>();
	
	static {
		cityMap.put(1L, "北京"); 
		productCityMap.put(-1L, 1L);
	}
	
	public static String getCityName(Long cityId) {
		return cityMap.get(cityId);
	}
	
	public static Long getCityId(Long productId) {
		return productCityMap.get(productId);
	}
	
}
