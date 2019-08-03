package com.ls.lishuai.java.copy;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/25 18:07
 */
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class BeanUtilTest {
    public static void main(String[] args) {
        Person per = new Person();
        Person per1 = new Person();
        per.setName("zhangsan");
        per.setSex("男");
        per.setAge(20);
        per.setBirthday(new Date());
        Dog dog = new Dog();
        dog.setDogName("1111111111111111");
        per.setDog(dog);
        try {
            BeanUtils.copyProperties(per1, per);
            per.setName("666666666666");
            Dog dog1 = per.getDog();
            dog1.setDogName("2222222222222222");
            Dog dog2 = per1.getDog();
            dog2.setDogName("33333333333333");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(per.toString());
        System.out.println(per1.toString());

    }
}

