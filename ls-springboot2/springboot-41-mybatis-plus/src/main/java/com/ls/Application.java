package com.ls;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-13 23:16
 */
@SpringBootApplication
@MapperScan("com.ls.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
