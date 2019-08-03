package com.ls;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "com.ls")
public class FooProperties {

    private String foo;

    private String databasePlatform;

}