package com.ls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 10:28
 */
@Component
public class BlogProperties {

    @Value("${com.ls.blog.name}")
    private String name;
    @Value("${com.ls.blog.title}")
    private String title;
    @Value("${com.ls.blog.desc}")
    private String desc;

    @Value("${com.ls.blog.value}")
    private String value;
    @Value("${com.ls.blog.number}")
    private Integer number;
    @Value("${com.ls.blog.bignumber}")
    private Long bignumber;
    @Value("${com.ls.blog.test1}")
    private Integer test1;
    @Value("${com.ls.blog.test2}")
    private Integer test2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getBignumber() {
        return bignumber;
    }

    public void setBignumber(Long bignumber) {
        this.bignumber = bignumber;
    }

    public Integer getTest1() {
        return test1;
    }

    public void setTest1(Integer test1) {
        this.test1 = test1;
    }

    public Integer getTest2() {
        return test2;
    }

    public void setTest2(Integer test2) {
        this.test2 = test2;
    }


    @Override
    public String toString() {
        return "BlogProperties{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", value='" + value + '\'' +
                ", number=" + number +
                ", bignumber=" + bignumber +
                ", jdk=" + test1 +
                ", test2=" + test2 +
                '}';
    }
}

