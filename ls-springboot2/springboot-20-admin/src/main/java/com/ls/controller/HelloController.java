package com.ls.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 10:07
 */
@RestController
public class HelloController {

//    @RequestMapping("/")
//    public String hello(String name){
//        return "hello "+name;
//    }

    @GetMapping("/index")
    public String index() {
        return "index1";
    }

}
