动态代理

在java的动态代理机制中，有两个重要的类或接口，一个是 InvocationHandler(Interface)、
另一个则是 Proxy(Class)，这一个类和接口是实现我们动态代理所必须用到的。

每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，
当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的 invoke 方法来进行调用

public interface InvocationHandler {
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
proxy:　　指代我们所代理的那个真实对象
method:　　指代的是我们所要调用真实对象的某个方法的Method对象
args:　　指代的是调用真实对象某个方法时接受的参数

Proxy这个类的作用就是用来动态创建一个代理对象的类，它提供了许多的方法，但是我们用的最多的就是 newProxyInstance 这个方法：
public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces,  InvocationHandler h)  throws IllegalArgumentException
这个方法的作用就是得到一个动态的代理对象，其接收三个参数，我们来看看这三个参数所代表的含义：

loader:　　一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
interfaces:　　一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
h:　　一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上


v1包的client类运行的结果分析：
com.sun.proxy.$Proxy0
before rent house
Method:public abstract void com.ls.design_pattern.Proxy.dynamic.jdk.Subject.rent()
I want to rent my house
after rent house
before rent house
Method:public abstract void com.ls.design_pattern.Proxy.dynamic.jdk.Subject.hello(java.lang.String)
hello: world
after rent house
before rent house
Method:public abstract java.lang.String com.ls.design_pattern.Proxy.dynamic.jdk.Subject.re(java.lang.String)
after rent house
1111111111111111111111111222222222222


我们首先来看看 $Proxy0 这东西，我们看到，这个东西是由 System.out.println(subject.getClass().getName()); 
这条语句打印出来的，那么为什么我们返回的这个代理对象的类名是这样的呢？

Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
可能我以为返回的这个代理对象会是Subject类型的对象，或者是InvocationHandler的对象，结果却不是，
首先我们解释一下为什么我们这里可以将其转化为Subject类型的对象？原因就是在newProxyInstance这个方法的第二个参数上，
我们给这个代理对象提供了一组什么接口，那么我这个代理对象就会实现了这组接口，这个时候我们当然可以将这个代理对象强制类型转化为这组接口中的任意一个，
因为这里的接口是Subject类型，所以就可以将其转化为Subject类型了。

同时我们一定要记住，通过 Proxy.newProxyInstance 创建的代理对象是在jvm运行时动态生成的一个对象，它并不是我们的InvocationHandler类型，
也不是我们定义的那组接口的类型，而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以$开头，proxy为中，最后一个数字表示对象的标号。

接着我们来看看这两句 

subject.rent();
subject.hello("world");

这里是通过代理对象来调用实现的那种接口中的方法，这个时候程序就会跳转到由这个代理对象关联到的 handler 中的invoke方法去执行，
而我们的这个 handler 对象又接受了一个 RealSubject类型的参数，表示我要代理的就是这个真实对象，所以此时就会调用 handler 中的invoke方法去执行：


public Object invoke(Object object, Method method, Object[] args)
            throws Throwable
    {
        //　　在代理真实对象前我们可以添加一些自己的操作
        System.out.println("before rent house");
        
        System.out.println("Method:" + method);
        
        //    当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        method.invoke(subject, args);
        
        //　　在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("after rent house");
        
        return null;
    }

我们看到，在真正通过代理对象来调用真实对象的方法的时候，我们可以在该方法前后添加自己的一些操作，同时我们看到我们的这个 method 对象是这样的：
public abstract void com.xiaoluo.dynamicproxy.Subject.rent()
public abstract void com.xiaoluo.dynamicproxy.Subject.hello(java.lang.String)
正好就是我们的Subject接口中的两个方法，这也就证明了当我通过代理对象来调用方法的时候，
起实际就是委托由其关联到的 handler 对象的invoke方法中来调用，并不是自己来真实调用，而是通过代理的方式来调用的。
