package com.ls.lishuai.java.copy;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/25 18:12
 */
public class Dog {
    public String dogName;

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogName='" + dogName + '\'' +
                '}';
    }
}
