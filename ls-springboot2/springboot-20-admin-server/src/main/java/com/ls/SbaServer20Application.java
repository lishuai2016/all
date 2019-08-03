package com.ls;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 23:29
 */
@SpringBootApplication
@EnableAdminServer
public class SbaServer20Application {

    public static void main(String[] args) {
        SpringApplication.run(SbaServer20Application.class, args);
    }
}