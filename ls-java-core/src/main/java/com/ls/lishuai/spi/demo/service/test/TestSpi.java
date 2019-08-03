package com.ls.lishuai.spi.demo.service.test;

import com.ls.lishuai.spi.demo.service.HelloService;

import java.util.ServiceLoader;

public class TestSpi {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("1111");
		ServiceLoader<HelloService> helloServiceLoader=ServiceLoader.load(HelloService.class);
		for(HelloService item : helloServiceLoader){
		    item.say();
		}
	}

}
