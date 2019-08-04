package com.roncoo.eshop.cache.ha.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class FailureModeCommand extends HystrixCommand<Boolean> {

	private boolean failure;
	
	public FailureModeCommand(boolean failure) {
		super(HystrixCommandGroupKey.Factory.asKey("FailureModeGroup"));
		this.failure = failure;
	}
	
	@Override
	protected Boolean run() throws Exception {
		if(failure) {
			throw new Exception();
		}
		return true;
	}
	
	@Override
	protected Boolean getFallback() {
		return false;
	}
	
}
