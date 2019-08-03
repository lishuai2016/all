package com.ls.hadoop.program.weibo.friend;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class FoFSort extends WritableComparator {

	public FoFSort() {
		super(User.class,true);
	}
	
	public int compare(WritableComparable a, WritableComparable b) {
		User u1 =(User) a;
		User u2=(User) b;
		
		int result =u1.getUname().compareTo(u2.getUname());
		if(result==0){
			return -Integer.compare(u1.getFriendsCount(), u2.getFriendsCount());
		}
		return result;
	}
}
