package com.ls.lishuai.spi.demo.service.impl;

import com.ls.lishuai.spi.demo.service.HelloService;

public class DefaultService implements HelloService {

	@Override
	public void say() {
		System.out.println( "DefaultService Hello World!" );

	}

}
