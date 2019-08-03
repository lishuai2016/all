package com.ls.hadoop.program.weibo.friend;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FoFGroup extends WritableComparator {

	public FoFGroup() {
		super(User.class,true);
	}
	
	public int compare(WritableComparable a, WritableComparable b) {
		User u1 =(User) a;
		User u2=(User) b;
		
		return u1.getUname().compareTo(u2.getUname());
	}
}
