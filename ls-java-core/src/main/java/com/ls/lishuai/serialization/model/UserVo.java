package com.ls.lishuai.serialization.model;

import java.io.Serializable;
import java.util.List;

public class UserVo implements Serializable {
	 @Override
	public String toString() {
		return "UserVo [name=" + name + ", age=" + age + ", phone=" + phone
				+ ", friends=" + friends + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1934482881976673113L;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public List<UserVo> getFriends() {
		return friends;
	}
	public void setFriends(List<UserVo> friends) {
		this.friends = friends;
	}
	private String name;
	 private int age;  
	 private long phone;  	      
	 private List<UserVo> friends;
}
