package com.ls.design_pattern.Proxy;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 18:08
 *
 *
 *可以看到，SubjectProxy实现了Subject接口（和RealSubject实现相同接口），并持有的是Subject接口类型的引用。
 * 这样调用的依然是doSomething方法，只是实例化对象的过程改变了，结果来看，
 * 代理类SubjectProxy可以自动为我们加上了before和after等我们需要的动作。
如果将来需要实现一个新的接口，就需要在代理类里再写该接口的实现方法，
对导致代理类的代码变得臃肿；另一方面，当需要改变抽象角色接口时，无疑真实角色和代理角色也需要改变。
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Subject sub = new SubjectProxy();
        sub.doSomething();
    }
}
