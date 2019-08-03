[Spring中的反射机制浅析](https://blog.csdn.net/woshixuye/article/details/7700455)
[Spring MVC 中 HandlerInterceptorAdapter的使用](https://blog.csdn.net/liuwenbo0920/article/details/7283757)
[第五章 处理器拦截器详解——跟着开涛学SpringMVC ](http://sishuok.com/forum/blogPost/list/5934.html)
[关于spring,IOC和AOP的解析原理和举例 ](http://blog.sina.com.cn/s/blog_624a352c0101fo9j.html)
[java动态代理（JDK和cglib）](http://www.cnblogs.com/jqyp/archive/2010/08/20/1805041.html)
[Spring3：AOP](http://www.cnblogs.com/xrq730/p/4919025.html)
[Spring 源码分析(一) —— 迈向Spring之路](https://my.oschina.net/kaywu123/blog/610825)
[Spring：源码解读Spring IOC原理](http://www.cnblogs.com/ITtangtang/p/3978349.html)
[spring IOC源码分析（1）](https://blog.csdn.net/shi1122/article/details/6735423)
[Spring AOP 实现原理](https://blog.csdn.net/moreevan/article/details/11977115)
[org.springframework.web.util.IntrospectorCleanupListener](https://blog.csdn.net/jndxjing/article/details/6234637)
[Spring整合 RMI](https://blog.csdn.net/arkblue/article/details/6237380)
[史上最全最强SpringMVC详细示例实战教程](http://www.admin10000.com/document/6436.html)
[跟开涛学SpringMVC](https://jinnianshilongnian.iteye.com/category/231099)
[SpringMVC中使用Interceptor拦截器](https://elim.iteye.com/blog/1750680)
[SpringMVC源码剖析（一）- 从抽象和接口说起](https://my.oschina.net/lichhao/blog/99039)
[SpringMVC源码剖析（五)-消息转换器HttpMessageConverter](https://my.oschina.net/lichhao/blog/172562)
[SpringMVC源码剖析系列](https://my.oschina.net/lichhao?tab=newest&catalogId=285356)
[SpringMVC深度探险收藏](https://www.iteye.com/blogs/subjects/springmvc-explore)
[实例详解Spring MVC入门使用](https://blog.csdn.net/kkdelta/article/details/7274708)
[spring MVC配置详解](http://www.cnblogs.com/superjt/p/3309255.html)
[Spring MVC的多视图解析器配置及与Freemarker的集成](http://www.tashan10.com/spring-mvcde-duo-shi-tu-jie-xi-qi-pei-zhi/)
[Spring MVC 中 HandlerInterceptorAdapter的使用](https://blog.csdn.net/liuwenbo0920/article/details/7283757)
[spring mvc controller间跳转 重定向 传参 （转）](http://www.cnblogs.com/youngjoy/p/3919656.html)




spring和springMVC学习链接
http://www.360doc.com/userhome.aspx?userid=18637323&cid=18

http://jinnianshilongnian.iteye.com/blog/1602617



http://www.zuidaima.com/



springMVC源码学习：
http://my.oschina.net/lichhao/blog
http://downpour.iteye.com/blog/1330537



spring源码学习：
Spring：源码解读Spring IOC原理http://www.cnblogs.com/ITtangtang/p/3978349.html






Spring的IOC和aop原理（总结）
1、IOC（反射机制）

　那为什么说IOC很简单呢？说白了其实就是由我们平常的new转成了使用反射来获取类的实例，相信任何人只要会用java的反射机制，那么自己写一个IOC框架也不是不可能的。比如：

public ObjectgetInstance(String className) throws Exception
{
　　Object obj = Class.forName(className).newInstance();
　　Method[] methods = obj.getClass().getMethods();
　　for (Method method : methods) {
　　　　if (method.getName().intern() == "setString") {
　　　　　　method.invoke(obj, "hello world!");
　　　　}
　　}
}
　　上面的一个方法我们就很简单的使用了反射为指定的类的setString方法来设置一个hello world!字符串。其实可以看到IOC真的很简单，当然了IOC简单并不表示spring的IOC就简单，spring的IOC的功能强大就在于有一系列非常强大的配置文件维护类，它们可以维护spring配置文件中的各个类的关系，这才是spring的IOC真正强大的地方。在spring的Bean定义文件中，不仅可以为定义Bean设置属性，还支持Bean之间的继承、Bean的抽象和不同的获取方式等等功能。

2、aop（反射+动态代理）


Spring AOP的几个概念
1.切面(Aspect)：切面就是一个关注点的模块化，如事务管理、日志管理、权限管理等；

2.连接点(Joinpoint)：程序执行时的某个特定的点，在Spring中就是一个方法的执行；

3.通知(Advice)：通知就是在切面的某个连接点上执行的操作，也就是事务管理、日志管理等；

4.切入点(Pointcut)：切入点就是描述某一类选定的连接点，也就是指定某一类要织入通知的方法；

5.目标对象(Target)：就是被AOP动态代理的目标对象；

<aop:config>
            <aop:aspect id="time" ref="timeHandler">
                <aop:pointcut id="addAllMethod" expression="execution(* com.xrq.aop.HelloWorld.*(..))" />
                <aop:before method="printTime" pointcut-ref="addAllMethod" />
                <aop:after method="printTime" pointcut-ref="addAllMethod" />
            </aop:aspect>
        </aop:config>




spring aop原理
什么是AOPAOP（Aspect-OrientedProgramming，面向方面编程），可以说是OOP（Object-Oriented Programing，面向对象编程）的补充和完善。OOP引入封装、继承和多态性等概念来建立一种对象层次结构，用以模拟公共行为的一个集合。当我们需要为分散的对象引入公共行为的时候，OOP则显得无能为力。也就是说，OOP允许你定义从上到下的关系，但并不适合定义从左到右的关系。例如日志功能。日志代码往往水平地散布在所有对象层次中，而与它所散布到的对象的核心功能毫无关系。对于其他类型的代码，如安全性、异常处理和透明的持续性也是如此。这种散布在各处的无关的代码被称为横切（cross-cutting）代码，在OOP设计中，它导致了大量代码的重复，而不利于各个模块的重用。
 
而AOP技术则恰恰相反，它利用一种称为“横切”的技术，剖解开封装的对象内部，并将那些影响了多个类的公共行为封装到一个可重用模块，并将其名为“Aspect”，即方面。所谓“方面”，简单地说，就是将那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可操作性和可维护性。AOP代表的是一个横向的关系，如果说“对象”是一个空心的圆柱体，其中封装的是对象的属性和行为；那么面向方面编程的方法，就仿佛一把利刃，将这些空心圆柱体剖开，以获得其内部的消息。而剖开的切面，也就是所谓的“方面”了。然后它又以巧夺天功的妙手将这些剖开的切面复原，不留痕迹。
 
使用“横切”技术，AOP把软件系统分为两个部分：核心关注点和横切关注点。业务处理的主要流程是核心关注点，与之关系不大的部分是横切关注点。横切关注点的一个特点是，他们经常发生在核心关注点的多处，而各处都基本相似。比如权限认证、日志、事务处理。Aop 的作用在于分离系统中的各种关注点，将核心关注点和横切关注点分离开来。正如Avanade公司的高级方案构架师Adam Magee所说，AOP的核心思想就是“将应用程序中的商业逻辑同对其提供支持的通用服务进行分离。”
 
实现AOP的技术，主要分为两大类：一是采用动态代理技术，利用截取消息的方式，对该消息进行装饰，以取代原有对象行为的执行；二是采用静态织入的方式，引入特定的语法创建“方面”，从而使得编译器可以在编译期间织入有关“方面”的代码。



controller404问题

查看
spring-mvc.xml文件的
 <!-- 自动扫描范畴-->
     <context:component-scan base-package="test" />

  <!-- MVC 注解掃描 -->
<mvc:annotation-driven />
是否存在











