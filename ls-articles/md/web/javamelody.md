---
title: javamelody
categories: 
- web
tags:
---


javamelody


https://github.com/javamelody/javamelody/blob/master/javamelody-for-spring-boot/pom.xml

https://github.com/javamelody/javamelody/wiki/SpringBootStarter


  <!-- javamelody-core -->
        <dependency>
            <groupId>net.bull.javamelody</groupId>
            <artifactId>javamelody-core</artifactId>
            <version>1.74.0</version>
        </dependency>

        <!-- itext, option to add PDF export -->
        <dependency>
            <groupId>com.lowagie</groupId>
            <artifactId>itext</artifactId>
            <version>2.1.7</version>
            <exclusions>
                <exclusion>
                    <artifactId>bcmail-jdk14</artifactId>
                    <groupId>bouncycastle</groupId>
                </exclusion>
                <exclusion>
                    <artifactId>bcprov-jdk14</artifactId>
                    <groupId>bouncycastle</groupId>
                </exclusion>
                <exclusion>
                    <artifactId>bctsp-jdk14</artifactId>
                    <groupId>bouncycastle</groupId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>









        @Configuration
public class FilterConfigBean {

    @Bean
    public FilterRegistrationBean filterRegistration(){
        // 新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加自定义 过滤器
        registration.setFilter(new MonitoringFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        //设置过滤器顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public MonitoringFilter createMonitoringFilter() {
        MonitoringFilter filter = new MonitoringFilter();





        return filter;
    }

    /**
     *  配置javamelody监听器sessionListener
     */
    @Bean
    public ServletListenerRegistrationBean<SessionListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<SessionListener> slrBean = new ServletListenerRegistrationBean<SessionListener>();
        slrBean.setListener(new SessionListener());
        return slrBean;
    }


}




#javamelody 监控
javamelody.enabled=true
javamelody.spring-monitoring-enabled=true
javamelody.init-parameters.log=true
javamelody.authorized-users=user1:pwd1

问题：springboot中的日志配置补齐作用？？？？