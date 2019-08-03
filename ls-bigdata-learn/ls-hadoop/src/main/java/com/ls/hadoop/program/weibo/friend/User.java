package com.ls.hadoop.program.weibo.friend;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class User implements WritableComparable<User> {

	private String uname;
	private int friendsCount;
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String uname, int friendsCount){
		this.uname=uname;
		this.friendsCount=friendsCount;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeUTF(uname);
		out.writeInt(friendsCount);
	}

	
	public void readFields(DataInput in) throws IOException {
		this.uname=in.readUTF();
		this.friendsCount=in.readInt();
	}

	public int compareTo(User o) {
		int result = this.uname.compareTo(o.getUname());
		if(result==0){
			return Integer.compare(this.friendsCount, o.getFriendsCount());
		}
		return result;
	}

	
	
}
