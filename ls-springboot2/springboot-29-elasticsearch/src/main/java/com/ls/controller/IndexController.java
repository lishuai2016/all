package com.ls.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-17 20:55
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "elasticsearch";
    }
}
