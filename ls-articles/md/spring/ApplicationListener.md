---
title: ApplicationListener
categories: 
- spring
tags:
---


# ApplicationListener使用场景
在一些业务场景中，当容器初始化完成之后，需要处理一些操作，比如一些数据的加载、初始化缓存、特定任务的注册等等。
这个时候我们就可以使用Spring提供的ApplicationListener来进行操作。

用法：
1、首先，需要实现ApplicationListener接口并实现onApplicationEvent方法。把需要处理的操作放在onApplicationEvent中进行处理：

```java
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent>{
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("我的父容器为：" + contextRefreshedEvent.getApplicationContext().getParent());
        System.out.println("初始化时我被调用了。");
    }
}
```
2、然后，实例化ApplicationStartListener这个类，在Spring boot中通过一个配置类来进行实例化：

```java
@Configuration
public class ListenerConfig {
    @Bean
    public ApplicationStartListener applicationStartListener(){
        return new ApplicationStartListener();
    }
}
```


3、随后，启动Spring boot服务，打印出一下内容：
我的父容器为：null
初始化时我被调用了。
从打印的结果可以看出，ApplicationStartListener的onApplicationEvent方法在容器启动时已经被成功调用了。而此时初始化的容器为root容器。




# 二次调用问题
此处使用Spring boot来进行操作，没有出现二次调用的问题。在使用传统的application.xml和project-servlet.xml配置中会出现二次调用的问题。
主要原因是初始化root容器之后，会初始化project-servlet.xml对应的子容器。我们需要的是只执行一遍即可。
那么上面打印父容器的代码用来进行判断排除子容器即可。在业务处理之前添加如下判断：

if(contextRefreshedEvent.getApplicationContext().getParent() != null){
            return;
}
这样其他容器的初始化就会直接返回，而父容器（Parent为null的容器）启动时将会执行相应的业务操作。

关联知识
在spring中InitializingBean接口也提供了类似的功能，只不过它进行操作的时机是在所有bean都被实例化之后才进行调用。
根据不同的业务场景和需求，可选择不同的方案来实现。


