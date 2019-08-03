package com.ls.controller;


import com.ls.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-13 23:16
 */
@RestController
public class HelloController {

    @Autowired
    private UserMapper userMapper;



    @Value("${ls.name}")
    private String name;

    @GetMapping("/")
    public Object index() {
        return "name:"+name+userMapper.selectList(null);
    }
}
