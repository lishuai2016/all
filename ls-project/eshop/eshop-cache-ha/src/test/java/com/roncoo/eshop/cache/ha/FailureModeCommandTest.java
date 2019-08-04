package com.roncoo.eshop.cache.ha;

import com.roncoo.eshop.cache.ha.hystrix.command.FailureModeCommand;

public class FailureModeCommandTest {
	
	public static void main(String[] args) {
		try {
			FailureModeCommand failureModeCommand = new FailureModeCommand(true);
			System.out.println(failureModeCommand.execute()); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
