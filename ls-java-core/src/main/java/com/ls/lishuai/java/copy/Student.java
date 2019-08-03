package com.ls.lishuai.java.copy;

import java.util.Date;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/25 18:07
 */
public class Student {
    private String name;
    private int age;
    private String address;
    private Date birthday;

    private Double account;

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


}

