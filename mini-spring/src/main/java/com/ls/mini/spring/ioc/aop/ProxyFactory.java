package com.ls.mini.spring.ioc.aop;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-23 11:46
 *  代理工厂生成jdk代理或者cglib代理对象
 *
 *  在这里可以根据类的性质动态替不同代理的实现形式
 *
 *  根据参数AdvisedSupport来生成具体的代理对象
 */
public class  ProxyFactory  implements AopProxy{

    private AdvisedSupport advised;

    public ProxyFactory(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return createCglib2AopProxy();
    }



    protected final Object createCglib2AopProxy() {
        return new Cglib2AopProxy(advised).getProxy();
    }

    protected final Object createJdkDynamicAopProxy() {
        return new JdkDynamicAopProxy(advised).getProxy();
    }
}
