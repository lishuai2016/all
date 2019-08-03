package com.ls.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 12:02
 */
@ApiModel
public class User {

    @ApiModelProperty(value = "用户id")
    private Long id;
    @NotNull(message = "用户名称不能为空")
    @ApiModelProperty(value = "用户名称", required = true)
    private String name;
    @NotNull(message = "用户年龄不能为空")
    @ApiModelProperty(value = "用户年龄", required = true)
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
