package com.ls.hadoop.program.weibo.weather;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyGroup extends WritableComparator {

	public MyGroup(){
		super(MyKey.class,true);
	}
	
	public int compare(WritableComparable a, WritableComparable b) {
		MyKey k1 =(MyKey) a;
		MyKey k2 =(MyKey) b;
		int r1 = Integer.compare(k1.getYear(), k2.getYear());
		if(r1==0){
			return Integer.compare(k1.getMonth(), k2.getMonth());
		}else{
			return r1;
		}
		
	}
}
