package com.ls.hadoop.program.weibo.tf;

import java.text.NumberFormat;

public class Test {

	public static void main(String[] args) {
		double s=34 * Math.log(1056/5);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(5);
		System.out.println(nf.format(s));
	}
}
