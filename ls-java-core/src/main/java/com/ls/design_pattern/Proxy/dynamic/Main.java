package com.ls.design_pattern.Proxy.dynamic;

import com.ls.design_pattern.Proxy.RealSubject;
import com.ls.design_pattern.Proxy.Subject;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 18:15
 * 　在调用过程中使用了通用的代理类包装了RealSubject实例，
 * 然后调用了Jdk的代理工厂方法实例化了一个具体的代理类。
 * 最后调用代理的doSomething方法，还有附加的before、after方法可以被任意复用
 * （只要我们在调用代码处使用这个通用代理类去包装任意想要需要包装的被代理类即可）。
 * 当接口改变的时候，虽然被代理类需要改变，但是我们的代理类却不用改变了。
 * 这个调用虽然足够灵活，可以动态生成一个具体的代理类，而不用自己显示的创建一个实现具体接口的代理类。
 */
public class Main {
    public static void main(String args[]) {
        ProxyHandler proxy = new ProxyHandler();
        //绑定该类实现的所有接口
        Subject sub = (Subject) proxy.bind(new RealSubject());
        sub.doSomething();
    }
}
