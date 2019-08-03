package com.ls.web.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08 10:31
 */
@RestController
public class IndexController {


    @RequestMapping("/")
    public String index() {
        System.out.println("IndexController的index方法被调用");
        return "hello";
    }
}
