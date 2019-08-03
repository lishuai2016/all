package com.ls.web.annotation;

import java.lang.annotation.*;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-09
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface ServiceLimit {
    String description()  default "";
}
