package com.ls.redis.miaosha;

import com.alibaba.fastjson.JSON;

public class Util {
	public static  String beanToJson(Object o){
		return JSON.toJSONString(o);
	}
	//parse an object from 
	public static <T> T jsonToBean(String json,Class<T> cls){
		return JSON.parseObject(json, cls);
	} 
}
