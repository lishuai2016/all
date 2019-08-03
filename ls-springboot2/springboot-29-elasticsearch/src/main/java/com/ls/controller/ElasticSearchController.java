package com.ls.controller;

import com.google.gson.Gson;
import com.ls.domain.Employee;
import com.ls.domain.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-21
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

    @Autowired
    private EmployeeRepository er;

    //增加
    @RequestMapping("/add/{id}")
    public String add(@PathVariable("id")String id){

        Employee employee=new Employee();
        employee.setId(id);
        employee.setFirstName("Y.S.K");
        employee.setLastName("~");
        employee.setAge(26);
        employee.setAbout("");
        er.save(employee);

        System.err.println("add a obj");

        return "success";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(){
        Employee employee=new Employee();
        employee.setId("1");
        er.delete(employee);

        return "success";
    }

    //局部更新
    @RequestMapping("/update")
    public String update(){

        Employee employee=er.queryEmployeeById("1");
        employee.setFirstName("哈哈");
        er.save(employee);

        System.err.println("update a obj");

        return "success";
    }

    //查询
    @RequestMapping("/query/{id}")
    public Employee query(@PathVariable("id")String id){

        Employee accountInfo=er.queryEmployeeById(id);
        System.err.println(new Gson().toJson(accountInfo));

        return accountInfo;
    }

}

