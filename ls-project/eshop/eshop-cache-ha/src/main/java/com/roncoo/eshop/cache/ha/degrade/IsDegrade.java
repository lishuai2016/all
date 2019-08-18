package com.roncoo.eshop.cache.ha.degrade;



//封装降级标识

public class IsDegrade {

	private static boolean degrade = false;

	public static boolean isDegrade() {
		return degrade;
	}

	public static void setDegrade(boolean degrade) {
		IsDegrade.degrade = degrade;
	}

}
