模拟spring IOC和aop的简单实现

# 参考

- [tiny-spring](https://github.com/lishuai2016/tiny-spring)

- [tiny-spring 分析](https://www.zybuluo.com/dugu9sword/note/382745)



# 版本说明

- 第一部分 IoC 容器的实现 对应了 tiny-spring 的 v1 到 v5 部分，这 5 个 step 实现了基本的 IoC 容器，
支持singleton类型的bean，包括初始化、属性注入、以及依赖 Bean 注入，可从 XML 中读取配置，XML 读取方式没有具体深入。

- 第二部分 AOP 容器的实现 对应了 tiny-spring 的 step-6 到 step-9 部分。step-10 中对 cglib 的支持没有分析。
这 4 个 step 可以使用 AspectJ 的语法进行 AOP 编写，支持接口代理。
考虑到 AspectJ 语法仅用于实现 execution("***") 部分的解析，不是主要内容，
也可以使用 Java 的正则表达式粗略地完成，因此没有关注这些细节。

> v1:container-register-and-get

新增：

简单模拟通过容器注册和获得bean的过程


> v2:abstract-beanfactory-and-do-bean-initilizing-in-it

新增：

模拟抽象spring的工厂类

- 定义了工厂接口BeanFactory：[抽象的工厂接口，定义了通过工厂获得bean和注册bean的方法],

- 一个抽象类AbstractBeanFactory：[实现了获得bean和注册bean的逻辑，通过一个map来维护bean信息,使用模板方法，定义一个抽象的创建bean的抽象方法doCreateBean]

- 一个工厂的默认实现AutowireCapableBeanFactory: [实现了模板方法doCreateBean，根据beanDefinition中的类对象，构建一个实例对象]

> v3:inject-bean-with-property

新增：

在BeanDefinition中新增PropertyValues字段，封装了构建bean的一些属性，用于bean的初始化。在com.ls.mini.spring.ioc.factory.AutowireCapableBeanFactory.doCreateBean中，
把实例化bean，分为两步：1、创建对象；2、设置bean的属性

> v4:config-beanfactory-with-xml

新增：

一个是资源定位Resource接口以及对应的实现类

一个是从resource定位的配置文件中读取bean的配置的接口BeanDefinitionReader，生成BeanDefinition

主要实现了从xml配置文件解析配置的bean信息，生成BeanDefinition并注册到bean的容器中。

> v5:inject-bean-to-bean

新增：

容器bean的懒加载机制，在初始化的时候仅仅是解析配置文件的信息到BeanDefinition然后注册到map中，
此时，并没有实例化BeanDefinition中的bean，在使用getbean的时候，会首先判断BeanDefinition是否注册，
没有的话报错，假如注册过的话，在检测BeanDefinition.getbean是否已经实例化了，没有的话进行实例化；

备注：这里面也出现了循环引用的问题。

比如：
```xml

    <bean name="outputService" class="com.ls.mini.spring.ioc.OutputService">
        <property name="helloWorldService" ref="helloWorldService"></property>
    </bean>

    <bean name="helloWorldService" class="com.ls.mini.spring.ioc.HelloWorldService">
        <property name="text" value="Hello World!"></property>
        <property name="outputService" ref="outputService"></property>
    </bean>
```

这里的解决方式是，在通过类构建实例的时候，还没有初始化属性时，把bean对象设置到BeanDefinition中，
这样出现依赖的对象之间获得对方的实例，不是完全的属性也初始化完毕，这样就避免了循环依赖的问题。


> v6:invite-application-context

调整包结构

新增接口ApplicationContext、AbstractApplicationContext、ClassPathXmlApplicationContext


> v7:method-interceptor-by-jdk-dynamic-proxy

引入了jar包：aopalliance

编写项目中aop包下的jdk动态代理，依赖于下面的两个接口

- org.aopalliance.intercept.MethodInterceptor

- org.aopalliance.intercept.MethodInvocation

> v8:invite-pointcut-and-aspectj

引入jar：aspectjweaver

接入BeanPostProcessor 初始化扩展接口以及通过expressin表达式匹配类或者方法的joinpoint


> v9:auto-create-aop-proxy && invite-cglib-and-aopproxy-factory

实现了通过xml配置【类似于spring配置aop切面】方式，实现自动创建aop逻辑，包含jdk动态代理和cglib动态代理


> v10、支持通过注解来实现aop

//todo


