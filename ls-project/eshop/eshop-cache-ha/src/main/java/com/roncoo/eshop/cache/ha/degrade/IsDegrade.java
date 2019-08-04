package com.roncoo.eshop.cache.ha.degrade;

public class IsDegrade {
	
	private static boolean degrade = false;

	public static boolean isDegrade() {
		return degrade;
	}

	public static void setDegrade(boolean degrade) {
		IsDegrade.degrade = degrade;
	}
	
}
