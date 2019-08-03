package com.ls.hadoop.program.weibo.weather;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MyKey implements WritableComparable<MyKey> {

	private int year;
	private int month;
	private double hot;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public double getHot() {
		return hot;
	}
	public void setHot(double hot) {
		this.hot = hot;
	}
	
	public void readFields(DataInput arg0) throws IOException {
		this.year=arg0.readInt();
		this.month=arg0.readInt();
		this.hot=arg0.readDouble();
	}
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(year);
		arg0.writeInt(month);
		arg0.writeDouble(hot);
	}
	
	//判断对象是否是同一个对象，当该对象作为输出的key
	public int compareTo(MyKey o) {
		int r1 = Integer.compare(this.year, o.getYear());
		if(r1==0){
			int r2 = Integer.compare(this.month, o.getMonth());
			if(r2==0){
				return  Double.compare(this.hot, o.getHot());
			}else{
				return r2;
			}
		}else{
			return r1;
		}
	}
	
}
