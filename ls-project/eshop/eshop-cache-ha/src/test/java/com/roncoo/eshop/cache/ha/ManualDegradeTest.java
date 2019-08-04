package com.roncoo.eshop.cache.ha;

import com.roncoo.eshop.cache.ha.degrade.IsDegrade;
import com.roncoo.eshop.cache.ha.hystrix.command.GetProductInfoFacadeCommand;

public class ManualDegradeTest {
	
	public static void main(String[] args) throws Exception {
		GetProductInfoFacadeCommand getProductInfoFacadeCommand1 = new GetProductInfoFacadeCommand(1L);
		System.out.println(getProductInfoFacadeCommand1.execute());  
		IsDegrade.setDegrade(true); 
		GetProductInfoFacadeCommand getProductInfoFacadeCommand2 = new GetProductInfoFacadeCommand(1L);
		System.out.println(getProductInfoFacadeCommand2.execute());  
	}
	
}
