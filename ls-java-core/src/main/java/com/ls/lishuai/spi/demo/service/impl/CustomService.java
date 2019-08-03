package com.ls.lishuai.spi.demo.service.impl;

import com.ls.lishuai.spi.demo.service.HelloService;

public class CustomService implements HelloService {

	@Override
	public void say() {
		 System.out.println( "CustomService Hello World!" );

	}

}
