package com.ls.web.config;

import com.ls.web.filter.FilterDemo01;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08 10:38
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new FilterDemo01());
        //过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("filterDemo01");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico");
        return registration;
    }
}
