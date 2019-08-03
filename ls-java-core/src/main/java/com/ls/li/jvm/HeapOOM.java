/**
 * 
 */
package com.ls.li.jvm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lishuai
 * @data 2017-3-1 上午9:09:12
 */

public class HeapOOM {
//	int num;
//    public HeapOOM() {
//        num = 100000;
//        // TODO Auto-generated constructor stub
//    }
//    public static void main(String[] args) {   
//        List<HeapOOM> list = new ArrayList<HeapOOM>();
//        while(true)
//            list.add(new HeapOOM());
//    }
	
	public static void main(String[] args)
	{
	Set<Person> set = new HashSet<Person>();
	Person p1 = new Person("唐僧","pwd1",25);
	Person p2 = new Person("孙悟空","pwd2",26);
	Person p3 = new Person("猪八戒","pwd3",27);
	set.add(p1);
	set.add(p2);
	set.add(p3);
	System.out.println("总共有:"+set.size()+" 个元素!"); //结果：总共有:3 个元素!
	p3.setAge(2); //修改p3的年龄,此时p3元素对应的hashcode值发生改变

	set.remove(p3); //此时remove不掉，造成内存泄漏

//	set.add(p3); //重新添加，居然添加成功
	System.out.println("总共有:"+set.size()+" 个元素!"); //结果：总共有:4 个元素!
//	for (Person person : set)
//	{
//	System.out.println(person);
//	}
	}
	
	
}

class Person{
	String name;
	String pwd;
	int age;
	public Person(String name,String pwd,int age) {
		this.name = name;
		this.pwd = pwd;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}