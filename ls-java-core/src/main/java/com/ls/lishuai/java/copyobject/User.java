package com.ls.lishuai.java.copyobject;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @Author: lishuai
 * @CreateDate: 2018/7/2 16:10
 */
public class User {
    private Integer id;
    private String username;
    private String gender;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception{
        User user=new User();
        BeanUtils.copyProperty(user, "id", "1");
        BeanUtils.copyProperty(user, "gender", "男");
        System.out.println("单个属性值传入的方式："+ user);
        User newUser=new User();
        BeanUtils.copyProperties(newUser, user);
        newUser.setGender("女");
        System.out.println("整个对象拷贝："+newUser);
        System.out.println("单个属性值传入的方式："+ user);





//        Map<String,Object> map=new HashMap<String,Object>();
//        map.put("username", "Jerry");
//        BeanUtils.populate(newUser, map);
//        System.out.println("拷贝map对象:"+newUser.getUsername());
    }
}

