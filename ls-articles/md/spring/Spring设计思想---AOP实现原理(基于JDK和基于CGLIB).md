---
title: Spring设计思想---AOP实现原理(基于JDK和基于CGLIB)
categories: 
- spring
tags:
---

Spring设计思想---AOP实现原理(基于JDK和基于CGLIB)

[参考](https://blog.csdn.net/luanlouis/article/details/51155821)

 源码地址：https://github.com/LuanLouis/thinking-in-spring.git
 
 
<div align="center"> <img src="/images/SpringAOP-1-1.png"/> </div><br>

目录
1、Spring内部创建代理对象的过程
2、Spring AOP的核心---ProxyFactoryBean
3、基于JDK面向接口的动态代理JdkDynamicAopProxy生成代理对象
4、基于Cglib子类继承方式的动态代理CglibAopProxy生成代理对象
5、各种Advice是的执行顺序是如何和方法调用进行结合的？
6、PointCut与Advice的结合------Adivce的条件执行
7、总结
    


# 1、Spring内部创建代理对象的过程
在Spring的底层，如果我们配置了代理模式，Spring会为每一个Bean创建一个对应的[ProxyFactoryBean]的[FactoryBean]来创建某个对象的代理对象。
假定我们现在有一个接口TicketService及其实现类RailwayStation，我们打算创建一个代理类，在执行TicketService的方法时的各个阶段，
插入对应的业务代码。

     package org.luanlouis.meditations.thinkinginspring.aop;
     
    /**
     * 售票服务
     * Created by louis on 2016/4/14.
     */
    public interface TicketService {
     
        //售票
        public void sellTicket();
     
        //问询
        public void inquire();
     
        //退票
        public void withdraw();
    }

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    /**
     * RailwayStation 实现 TicketService
     * Created by louis on 2016/4/14.
     */
    public class RailwayStation implements TicketService {
     
        public void sellTicket(){
            System.out.println("售票............");
        }
     
        public void inquire() {
            System.out.println("问询.............");
        }
     
        public void withdraw() {
            System.out.println("退票.............");
        }
    }

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.springframework.aop.MethodBeforeAdvice;
     
    import java.lang.reflect.Method;
     
    /**
     * 执行RealSubject对象的方法之前的处理意见
     * Created by louis on 2016/4/14.
     */
    public class TicketServiceBeforeAdvice implements MethodBeforeAdvice {
     
        public void before(Method method, Object[] args, Object target) throws Throwable {
            System.out.println("BEFORE_ADVICE: 欢迎光临代售点....");
        }
    }

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.springframework.aop.AfterReturningAdvice;
     
    import java.lang.reflect.Method;
     
    /**
     * 返回结果时后的处理意见
     * Created by louis on 2016/4/14.
     */
    public class TicketServiceAfterReturningAdvice implements AfterReturningAdvice {
        @Override
        public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
            System.out.println("AFTER_RETURNING：本次服务已结束....");
        }
    }

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.springframework.aop.ThrowsAdvice;
     
    import java.lang.reflect.Method;
     
    /**
     * 抛出异常时的处理意见
     * Created by louis on 2016/4/14.
     */
    public class TicketServiceThrowsAdvice implements ThrowsAdvice {
     
        public void afterThrowing(Exception ex){
            System.out.println("AFTER_THROWING....");
        }
        public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
            System.out.println("调用过程出错啦！！！！！");
        }
     
    } 
    
     package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.aopalliance.intercept.MethodInterceptor;
    import org.aopalliance.intercept.MethodInvocation;
    import org.springframework.aop.aspectj.AspectJAroundAdvice;
     
    /**
     *
     * AroundAdvice
     * Created by louis on 2016/4/15.
     */
    public class TicketServiceAroundAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("AROUND_ADVICE:BEGIN....");
            Object returnValue = invocation.proceed();
            System.out.println("AROUND_ADVICE:END.....");
            return returnValue;
        }
    }
<div align="center"> <img src="/images/SpringAOP-2-1.png"/> </div><br>

现在，我们来手动使用ProxyFactoryBean来创建Proxy对象，并将相应的几种不同的Advice加入这个proxy对应的各个执行阶段中：

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.aopalliance.aop.Advice;
    import org.springframework.aop.framework.ProxyFactoryBean;
     
    /**
     * 通过ProxyFactoryBean 手动创建 代理对象
     * Created by louis on 2016/4/14.
     */
    public class App {
     
        public static void main(String[] args) throws Exception {
     
            //1.针对不同的时期类型，提供不同的Advice
            Advice beforeAdvice = new TicketServiceBeforeAdvice();
            Advice afterReturningAdvice = new TicketServiceAfterReturningAdvice();
            Advice aroundAdvice = new TicketServiceAroundAdvice();
            Advice throwsAdvice = new TicketServiceThrowsAdvice();
     
            RailwayStation railwayStation = new RailwayStation();
     
            //2.创建ProxyFactoryBean,用以创建指定对象的Proxy对象
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
           //3.设置Proxy的接口
            proxyFactoryBean.setInterfaces(TicketService.class);
            //4. 设置RealSubject
            proxyFactoryBean.setTarget(railwayStation);
            //5.使用JDK基于接口实现机制的动态代理生成Proxy代理对象，如果想使用CGLIB，需要将这个flag设置成true
            proxyFactoryBean.setProxyTargetClass(true);
     
            //6. 添加不同的Advice
     
            proxyFactoryBean.addAdvice(afterReturningAdvice);
            proxyFactoryBean.addAdvice(aroundAdvice);
            proxyFactoryBean.addAdvice(throwsAdvice);
            proxyFactoryBean.addAdvice(beforeAdvice);
            proxyFactoryBean.setProxyTargetClass(false);
            //7通过ProxyFactoryBean生成Proxy对象
            TicketService ticketService = (TicketService) proxyFactoryBean.getObject();
            ticketService.sellTicket();
     
        }
    }


不出意外的话，你会得到如下的输出结果：
<div align="center"> <img src="/images/SpringAOP-2-2.png"/> </div><br>

你会看到，我们成功地创建了一个通过一个ProxyFactoryBean和真实的实例对象创建出了对应的代理对象，并将各个Advice加入到proxy代理对象中。
你会发现，在调用RailwayStation的sellticket()之前，成功插入了BeforeAdivce逻辑，而调用RailwayStation的sellticket()之后，
AfterReturning逻辑也成功插入了。
AroundAdvice也成功包裹了sellTicket()方法，只不过这个AroundAdvice发生的时机有点让人感到迷惑。
实际上，这个背后的执行逻辑隐藏了Spring AOP关于AOP的关于Advice调度最为核心的算法机制，这个将在本文后面详细阐述。
另外，本例中ProxyFactoryBean是通过JDK的针对接口的动态代理模式生成代理对象的，具体机制，请看下面关于ProxyFactoryBean的介绍。

# 2、Spring AOP的核心---ProxyFactoryBean
上面我们通过了纯手动使用ProxyFactoryBean实现了AOP的功能。现在来分析一下上面的代码：我们为ProxyFactoryBean提供了如下信息：
1). Proxy应该感兴趣的Adivce列表；
2). 真正的实例对象引用ticketService;
3).告诉ProxyFactoryBean使用基于接口实现的JDK动态代理机制实现proxy: 
4). Proxy应该具备的Interface接口：TicketService;
根据这些信息，ProxyFactoryBean就能给我们提供我们想要的Proxy对象了！那么，ProxyFactoryBean帮我们做了什么？
<div align="center"> <img src="/images/SpringAOP-2-3.png"/> </div><br>

Spring 使用工厂Bean模式创建每一个Proxy，对应每一个不同的Class类型，在Spring中都会有一个相对应的ProxyFactoryBean. 
以下是ProxyFactoryBean的类图。
<div align="center"> <img src="/images/SpringAOP-2-4.png"/> </div><br>

如上所示，对于生成Proxy的工厂Bean而言，它要知道对其感兴趣的Advice信息，而这类的信息，被维护到Advised中。
Advised可以根据特定的类名和方法名返回对应的AdviceChain，用以表示需要执行的Advice串。


# 3、基于JDK面向接口的动态代理JdkDynamicAopProxy生成代理对象
JdkDynamicAopProxy类实现了AopProxy，能够返回Proxy，并且，其自身也实现了InvocationHandler角色。
也就是说，当我们使用proxy时，我们对proxy对象调用的方法，都最终被转到这个类的invoke()方法中。

    public interface AopProxy {
        Object getProxy();
    
        Object getProxy(@Nullable ClassLoader var1);
    }

    final class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable {
            //省略若干...
        /** Proxy的配置信息，这里主要提供Advisor列表，并用于返回AdviceChain */
        private final AdvisedSupport advised;
     
        /**
         * Construct a new JdkDynamicAopProxy for the given AOP configuration.
         * @param config the AOP configuration as AdvisedSupport object
         * @throws AopConfigException if the config is invalid. We try to throw an informative
         * exception in this case, rather than let a mysterious failure happen later.
         */
        public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException {
            Assert.notNull(config, "AdvisedSupport must not be null");
            if (config.getAdvisors().length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
                throw new AopConfigException("No advisors and no TargetSource specified");
            }
            this.advised = config;
        }
     
     
        @Override
        public Object getProxy() {
            return getProxy(ClassUtils.getDefaultClassLoader());
        }
        //返回代理实例对象
        @Override
        public Object getProxy(ClassLoader classLoader) {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
            }
            Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised);
            findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
            //这里的InvocationHandler设置成了当前实例对象，即对这个proxy调用的任何方法，都会调用这个类的invoke()方法
            //这里的invoke方法被调用，动态查找Advice列表，组成ReflectMethodInvocation
            return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
        }
        /**
         * 对当前proxy调用其上的任何方法，都将转到这个方法上
             * Implementation of {@code InvocationHandler.invoke}.
         * <p>Callers will see exactly the exception thrown by the target,
         * unless a hook method throws an exception.
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodInvocation invocation;
            Object oldProxy = null;
            boolean setProxyContext = false;
     
            TargetSource targetSource = this.advised.targetSource;
            Class<?> targetClass = null;
            Object target = null;
     
            try {
                if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
                    // The target does not implement the equals(Object) method itself.
                    return equals(args[0]);
                }
                if (!this.hashCodeDefined && AopUtils.isHashCodeMethod(method)) {
                    // The target does not implement the hashCode() method itself.
                    return hashCode();
                }
                if (!this.advised.opaque && method.getDeclaringClass().isInterface() &&
                        method.getDeclaringClass().isAssignableFrom(Advised.class)) {
                    // Service invocations on ProxyConfig with the proxy config...
                    return AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
                }
     
                Object retVal;
     
                if (this.advised.exposeProxy) {
                    // Make invocation available if necessary.
                    oldProxy = AopContext.setCurrentProxy(proxy);
                    setProxyContext = true;
                }
     
                // May be null. Get as late as possible to minimize the time we "own" the target,
                // in case it comes from a pool.
                target = targetSource.getTarget();
                if (target != null) {
                    targetClass = target.getClass();
                }
     
                // Get the interception chain for this method.获取当前调用方法的拦截链
                List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
     
                // Check whether we have any advice. If we don't, we can fallback on direct
                // reflective invocation of the target, and avoid creating a MethodInvocation.
                //如果没有拦截链，则直接调用Joinpoint连接点的方法。
                if (chain.isEmpty()) {
                    // We can skip creating a MethodInvocation: just invoke the target directly
                    // Note that the final invoker must be an InvokerInterceptor so we know it does
                    // nothing but a reflective operation on the target, and no hot swapping or fancy proxying.
                    Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                    retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
                }
                else {
                    // We need to create a method invocation...
                    //根据给定的拦截链和方法调用信息，创建新的MethodInvocation对象，整个拦截链的工作逻辑都在这个ReflectiveMethodInvocation里 
                    invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
                    // Proceed to the joinpoint through the interceptor chain.
                    retVal = invocation.proceed();
                }
     
                // Massage return value if necessary.
                Class<?> returnType = method.getReturnType();
                if (retVal != null && retVal == target && returnType.isInstance(proxy) &&
                        !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
                    // Special case: it returned "this" and the return type of the method
                    // is type-compatible. Note that we can't help if the target sets
                    // a reference to itself in another returned object.
                    retVal = proxy;
                }
                else if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
                    throw new AopInvocationException(
                            "Null return value from advice does not match primitive return type for: " + method);
                }
                return retVal;
            }
            finally {
                if (target != null && !targetSource.isStatic()) {
                    // Must have come from TargetSource.
                    targetSource.releaseTarget(target);
                }
                if (setProxyContext) {
                    // Restore old proxy.
                    AopContext.setCurrentProxy(oldProxy);
                }
            }
        }
    }

# 4、基于Cglib子类继承方式的动态代理CglibAopProxy生成代理对象
基于Cglib子类继承方式的动态代理CglibAopProxy生成代理对象：

    package org.springframework.aop.framework;
    /**
     * CGLIB-based {@link AopProxy} implementation for the Spring AOP framework.
     *
     * <p>Formerly named {@code Cglib2AopProxy}, as of Spring 3.2, this class depends on
     * Spring's own internally repackaged version of CGLIB 3.</i>.
     */
    @SuppressWarnings("serial")
    class CglibAopProxy implements AopProxy, Serializable {
     
        // Constants for CGLIB callback array indices
        private static final int AOP_PROXY = 0;
        private static final int INVOKE_TARGET = 1;
        private static final int NO_OVERRIDE = 2;
        private static final int DISPATCH_TARGET = 3;
        private static final int DISPATCH_ADVISED = 4;
        private static final int INVOKE_EQUALS = 5;
        private static final int INVOKE_HASHCODE = 6;
     
     
        /** Logger available to subclasses; static to optimize serialization */
        protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);
     
        /** Keeps track of the Classes that we have validated for final methods */
        private static final Map<Class<?>, Boolean> validatedClasses = new WeakHashMap<Class<?>, Boolean>();
     
     
        /** The configuration used to configure this proxy */
        protected final AdvisedSupport advised;
     
        protected Object[] constructorArgs;
     
        protected Class<?>[] constructorArgTypes;
     
        /** Dispatcher used for methods on Advised */
        private final transient AdvisedDispatcher advisedDispatcher;
     
        private transient Map<String, Integer> fixedInterceptorMap;
     
        private transient int fixedInterceptorOffset;
     
     
        /**
         * Create a new CglibAopProxy for the given AOP configuration.
         * @param config the AOP configuration as AdvisedSupport object
         * @throws AopConfigException if the config is invalid. We try to throw an informative
         * exception in this case, rather than let a mysterious failure happen later.
         */
        public CglibAopProxy(AdvisedSupport config) throws AopConfigException {
            Assert.notNull(config, "AdvisedSupport must not be null");
            if (config.getAdvisors().length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
                throw new AopConfigException("No advisors and no TargetSource specified");
            }
            this.advised = config;
            this.advisedDispatcher = new AdvisedDispatcher(this.advised);
        }
     
        /**
         * Set constructor arguments to use for creating the proxy.
         * @param constructorArgs the constructor argument values
         * @param constructorArgTypes the constructor argument types
         */
        public void setConstructorArguments(Object[] constructorArgs, Class<?>[] constructorArgTypes) {
            if (constructorArgs == null || constructorArgTypes == null) {
                throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
            }
            if (constructorArgs.length != constructorArgTypes.length) {
                throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length +
                        ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
            }
            this.constructorArgs = constructorArgs;
            this.constructorArgTypes = constructorArgTypes;
        }
     
     
        @Override
        public Object getProxy() {
            return getProxy(null);
        }
     
        @Override
        public Object getProxy(ClassLoader classLoader) {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating CGLIB proxy: target source is " + this.advised.getTargetSource());
            }
     
            try {
                Class<?> rootClass = this.advised.getTargetClass();
                Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");
     
                Class<?> proxySuperClass = rootClass;
                if (ClassUtils.isCglibProxyClass(rootClass)) {
                    proxySuperClass = rootClass.getSuperclass();
                    Class<?>[] additionalInterfaces = rootClass.getInterfaces();
                    for (Class<?> additionalInterface : additionalInterfaces) {
                        this.advised.addInterface(additionalInterface);
                    }
                }
     
                // Validate the class, writing log messages as necessary.
                validateClassIfNecessary(proxySuperClass, classLoader);
     
                // Configure CGLIB Enhancer...
                Enhancer enhancer = createEnhancer();
                if (classLoader != null) {
                    enhancer.setClassLoader(classLoader);
                    if (classLoader instanceof SmartClassLoader &&
                            ((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
                        enhancer.setUseCache(false);
                    }
                }
                enhancer.setSuperclass(proxySuperClass);
                enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
                enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
                enhancer.setStrategy(new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));
     
                Callback[] callbacks = getCallbacks(rootClass);
                Class<?>[] types = new Class<?>[callbacks.length];
                for (int x = 0; x < types.length; x++) {
                    types[x] = callbacks[x].getClass();
                }
                // fixedInterceptorMap only populated at this point, after getCallbacks call above
                enhancer.setCallbackFilter(new ProxyCallbackFilter(
                        this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
                enhancer.setCallbackTypes(types);
     
                // Generate the proxy class and create a proxy instance.
                return createProxyClassAndInstance(enhancer, callbacks);
            }
            catch (CodeGenerationException ex) {
                throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                        this.advised.getTargetClass() + "]: " +
                        "Common causes of this problem include using a final class or a non-visible class",
                        ex);
            }
            catch (IllegalArgumentException ex) {
                throw new AopConfigException("Could not generate CGLIB subclass of class [" +
                        this.advised.getTargetClass() + "]: " +
                        "Common causes of this problem include using a final class or a non-visible class",
                        ex);
            }
            catch (Exception ex) {
                // TargetSource.getTarget() failed
                throw new AopConfigException("Unexpected AOP exception", ex);
            }
        }
     
        protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
            enhancer.setInterceptDuringConstruction(false);
            enhancer.setCallbacks(callbacks);
            return (this.constructorArgs != null ?
                    enhancer.create(this.constructorArgTypes, this.constructorArgs) :
                    enhancer.create());
        }
     
        /**
         * Creates the CGLIB {@link Enhancer}. Subclasses may wish to override this to return a custom
         * {@link Enhancer} implementation.
         */
        protected Enhancer createEnhancer() {
            return new Enhancer();
        }
     
     
     
        private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
            // Parameters used for optimisation choices...
            boolean exposeProxy = this.advised.isExposeProxy();
            boolean isFrozen = this.advised.isFrozen();
            boolean isStatic = this.advised.getTargetSource().isStatic();
     
            // Choose an "aop" interceptor (used for AOP calls).
            Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
     
            // Choose a "straight to target" interceptor. (used for calls that are
            // unadvised but can return this). May be required to expose the proxy.
            Callback targetInterceptor;
            if (exposeProxy) {
                targetInterceptor = isStatic ?
                        new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) :
                        new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
            }
            else {
                targetInterceptor = isStatic ?
                        new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) :
                        new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
            }
     
            // Choose a "direct to target" dispatcher (used for
            // unadvised calls to static targets that cannot return this).
            Callback targetDispatcher = isStatic ?
                    new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp();
     
            Callback[] mainCallbacks = new Callback[] {
                    aopInterceptor,  // for normal advice
                    targetInterceptor,  // invoke target without considering advice, if optimized
                    new SerializableNoOp(),  // no override for methods mapped to this
                    targetDispatcher, this.advisedDispatcher,
                    new EqualsInterceptor(this.advised),
                    new HashCodeInterceptor(this.advised)
            };
     
            Callback[] callbacks;
     
            // If the target is a static one and the advice chain is frozen,
            // then we can make some optimisations by sending the AOP calls
            // direct to the target using the fixed chain for that method.
            if (isStatic && isFrozen) {
                Method[] methods = rootClass.getMethods();
                Callback[] fixedCallbacks = new Callback[methods.length];
                this.fixedInterceptorMap = new HashMap<String, Integer>(methods.length);
     
                // TODO: small memory optimisation here (can skip creation for methods with no advice)
                for (int x = 0; x < methods.length; x++) {
                    List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
                    fixedCallbacks[x] = new FixedChainStaticTargetInterceptor(
                            chain, this.advised.getTargetSource().getTarget(), this.advised.getTargetClass());
                    this.fixedInterceptorMap.put(methods[x].toString(), x);
                }
     
                // Now copy both the callbacks from mainCallbacks
                // and fixedCallbacks into the callbacks array.
                callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
                System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
                System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
                this.fixedInterceptorOffset = mainCallbacks.length;
            }
            else {
                callbacks = mainCallbacks;
            }
            return callbacks;
        }
     
     
        /**
         * General purpose AOP callback. Used when the target is dynamic or when the
         * proxy is not frozen.
         */
        private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {
     
            private final AdvisedSupport advised;
     
            public DynamicAdvisedInterceptor(AdvisedSupport advised) {
                this.advised = advised;
            }
     
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                Object oldProxy = null;
                boolean setProxyContext = false;
                Class<?> targetClass = null;
                Object target = null;
                try {
                    if (this.advised.exposeProxy) {
                        // Make invocation available if necessary.
                        oldProxy = AopContext.setCurrentProxy(proxy);
                        setProxyContext = true;
                    }
                    // May be null. Get as late as possible to minimize the time we
                    // "own" the target, in case it comes from a pool...
                    target = getTarget();
                    if (target != null) {
                        targetClass = target.getClass();
                    }
                    List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
                    Object retVal;
                    // Check whether we only have one InvokerInterceptor: that is,
                    // no real advice, but just reflective invocation of the target.
                    if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                        // We can skip creating a MethodInvocation: just invoke the target directly.
                        // Note that the final invoker must be an InvokerInterceptor, so we know
                        // it does nothing but a reflective operation on the target, and no hot
                        // swapping or fancy proxying.
                        Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                        retVal = methodProxy.invoke(target, argsToUse);
                    }
                    else {
                        // We need to create a method invocation...
                        retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
                    }
                    retVal = processReturnType(proxy, target, method, retVal);
                    return retVal;
                }
                finally {
                    if (target != null) {
                        releaseTarget(target);
                    }
                    if (setProxyContext) {
                        // Restore old proxy.
                        AopContext.setCurrentProxy(oldProxy);
                    }
                }
            }
            //省略...
        }
     
     
        /**
         * Implementation of AOP Alliance MethodInvocation used by this AOP proxy.
         */
        private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
     
            private final MethodProxy methodProxy;
     
            private final boolean publicMethod;
     
            public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                    Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
     
                super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
                this.methodProxy = methodProxy;
                this.publicMethod = Modifier.isPublic(method.getModifiers());
            }
     
            /**
             * Gives a marginal performance improvement versus using reflection to
             * invoke the target when invoking public methods.
             */
            @Override
            protected Object invokeJoinpoint() throws Throwable {
                if (this.publicMethod) {
                    return this.methodProxy.invoke(this.target, this.arguments);
                }
                else {
                    return super.invokeJoinpoint();
                }
            }
        }
     
    }



# 5、各种Advice的执行顺序是如何和方法调用进行结合的？
JdkDynamicAopProxy 和CglibAopProxy只是创建代理方式的两种方式而已，实际上我们为方法调用添加的各种Advice的执行逻辑都是统一的。
在Spring的底层，会把我们定义的各个Adivce分别 包裹成一个 MethodInterceptor,这些Advice按照加入Advised顺序，构成一个AdivseChain.

比如我们上述的代码：

        //5. 添加不同的Advice
        proxyFactoryBean.addAdvice(afterReturningAdvice);
        proxyFactoryBean.addAdvice(aroundAdvice);
        proxyFactoryBean.addAdvice(throwsAdvice);
        proxyFactoryBean.addAdvice(beforeAdvice);
        proxyFactoryBean.setProxyTargetClass(false);
        //通过ProxyFactoryBean生成
        TicketService ticketService = (TicketService) proxyFactoryBean.getObject();
        ticketService.sellTicket();

当我们调用 ticketService.sellTicket()时，Spring会把这个方法调用转换成一个MethodInvocation对象，然后结合上述的我们添加的各种Advice,
组成一个ReflectiveMethodInvocation:
<div align="center"> <img src="/images/SpringAOP-2-5.png"/> </div><br>

各种Advice本质而言是一个方法调用拦截器，现在让我们看看各个Advice拦截器都干了什么？
<div align="center"> <img src="/images/SpringAOP-2-6.png"/> </div><br>


    /**
     * 包裹MethodBeforeAdvice的方法拦截器
     * Interceptor to wrap am {@link org.springframework.aop.MethodBeforeAdvice}.
     * Used internally by the AOP framework; application developers should not need
     * to use this class directly.
     *
     * @author Rod Johnson
     */
    @SuppressWarnings("serial")
    public class MethodBeforeAdviceInterceptor implements MethodInterceptor, Serializable {
     
        private MethodBeforeAdvice advice;
     
     
        /**
         * Create a new MethodBeforeAdviceInterceptor for the given advice.
         * @param advice the MethodBeforeAdvice to wrap
         */
        public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
            Assert.notNull(advice, "Advice must not be null");
            this.advice = advice;
        }
     
        @Override
        public Object invoke(MethodInvocation mi) throws Throwable {
            //在调用方法之前，先执行BeforeAdvice
            this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
            return mi.proceed();
        }
     
    }

    /**
     * 包裹AfterReturningAdvice的方法拦截器
     * Interceptor to wrap am {@link org.springframework.aop.AfterReturningAdvice}.
     * Used internally by the AOP framework; application developers should not need
     * to use this class directly.
     *
     * @author Rod Johnson
     */
    @SuppressWarnings("serial")
    public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice, Serializable {
     
        private final AfterReturningAdvice advice;
     
     
        /**
         * Create a new AfterReturningAdviceInterceptor for the given advice.
         * @param advice the AfterReturningAdvice to wrap
         */
        public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
            Assert.notNull(advice, "Advice must not be null");
            this.advice = advice;
        }
     
        @Override
        public Object invoke(MethodInvocation mi) throws Throwable {
            //先调用invocation
            Object retVal = mi.proceed();
            //调用成功后，调用AfterReturningAdvice
            this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
            return retVal;
        }
     
    }

    /**
     * Interceptor to wrap an after-throwing advice.
     *
     * <p>The signatures on handler methods on the {@code ThrowsAdvice}
     * implementation method argument must be of the form:<br>
     *
     * {@code void afterThrowing([Method, args, target], ThrowableSubclass);}
     *
     * <p>Only the last argument is required.
     *
     * <p>Some examples of valid methods would be:
     *
     * <pre class="code">public void afterThrowing(Exception ex)</pre>
     * <pre class="code">public void afterThrowing(RemoteException)</pre>
     * <pre class="code">public void afterThrowing(Method method, Object[] args, Object target, Exception ex)</pre>
     * <pre class="code">public void afterThrowing(Method method, Object[] args, Object target, ServletException ex)</pre>
     *
     * <p>This is a framework class that need not be used directly by Spring users.
     *
     * @author Rod Johnson
     * @author Juergen Hoeller
     */
    public class ThrowsAdviceInterceptor implements MethodInterceptor, AfterAdvice {
     
        private static final String AFTER_THROWING = "afterThrowing";
     
        private static final Log logger = LogFactory.getLog(ThrowsAdviceInterceptor.class);
     
     
        private final Object throwsAdvice;
     
        /** Methods on throws advice, keyed by exception class */
        private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap<Class<?>, Method>();
     
     
        /**
         * Create a new ThrowsAdviceInterceptor for the given ThrowsAdvice.
         * @param throwsAdvice the advice object that defines the exception
         * handler methods (usually a {@link org.springframework.aop.ThrowsAdvice}
         * implementation)
         */
        public ThrowsAdviceInterceptor(Object throwsAdvice) {
            Assert.notNull(throwsAdvice, "Advice must not be null");
            this.throwsAdvice = throwsAdvice;
     
            Method[] methods = throwsAdvice.getClass().getMethods();
            for (Method method : methods) {
                //ThrowsAdvice定义的afterThrowing方法是Handler方法
                if (method.getName().equals(AFTER_THROWING) &&
                        (method.getParameterTypes().length == 1 || method.getParameterTypes().length == 4) &&
                        Throwable.class.isAssignableFrom(method.getParameterTypes()[method.getParameterTypes().length - 1])
                    ) {
                    // Have an exception handler
                    this.exceptionHandlerMap.put(method.getParameterTypes()[method.getParameterTypes().length - 1], method);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Found exception handler method: " + method);
                    }
                }
            }
     
            if (this.exceptionHandlerMap.isEmpty()) {
                throw new IllegalArgumentException(
                        "At least one handler method must be found in class [" + throwsAdvice.getClass() + "]");
            }
        }
     
        public int getHandlerMethodCount() {
            return this.exceptionHandlerMap.size();
        }
     
        /**
         * Determine the exception handle method. Can return null if not found.
         * @param exception the exception thrown
         * @return a handler for the given exception type
         */
        private Method getExceptionHandler(Throwable exception) {
            Class<?> exceptionClass = exception.getClass();
            if (logger.isTraceEnabled()) {
                logger.trace("Trying to find handler for exception of type [" + exceptionClass.getName() + "]");
            }
            Method handler = this.exceptionHandlerMap.get(exceptionClass);
            while (handler == null && exceptionClass != Throwable.class) {
                exceptionClass = exceptionClass.getSuperclass();
                handler = this.exceptionHandlerMap.get(exceptionClass);
            }
            if (handler != null && logger.isDebugEnabled()) {
                logger.debug("Found handler for exception of type [" + exceptionClass.getName() + "]: " + handler);
            }
            return handler;
        }
     
        @Override
        public Object invoke(MethodInvocation mi) throws Throwable {
            //使用大的try，先执行代码，捕获异常
            try {
                return mi.proceed();
            }
            catch (Throwable ex) {
                //获取异常处理方法
                Method handlerMethod = getExceptionHandler(ex);
                //调用异常处理方法
                if (handlerMethod != null) {
                    invokeHandlerMethod(mi, ex, handlerMethod);
                }
                throw ex;
            }
        }
     
        private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable {
            Object[] handlerArgs;
            if (method.getParameterTypes().length == 1) {
                handlerArgs = new Object[] { ex };
            }
            else {
                handlerArgs = new Object[] {mi.getMethod(), mi.getArguments(), mi.getThis(), ex};
            }
            try {
                method.invoke(this.throwsAdvice, handlerArgs);
            }
            catch (InvocationTargetException targetEx) {
                throw targetEx.getTargetException();
            }
        }
     
    }

关于AroundAdivce,其本身就是一个MethodInterceptor，所以不需要额外做转换了。
细心的你会发现，在拦截器串中，每个拦截器最后都会调用MethodInvocation的proceed()方法。如果按照简单的拦截器的执行串来执行的话，
MethodInvocation的proceed()方法至少要执行N次(N表示拦截器Interceptor的个数)，因为每个拦截器都会调用一次proceed()方法。
更直观地讲，比如我们调用了ticketService.sellTicket()方法，那么，按照这个逻辑，我们会打印出四条记录：
售票............
售票............
售票............
售票............
这样我们肯定不是我们需要的结果！！！！因为按照我们的理解，只应该有一条"售票............"才对。
真实的Spring的方法调用过程能够控制这个逻辑按照我们的思路执行，
Spring将这个整个方法调用过程连同若干个Advice组成的拦截器链组合成ReflectiveMethodInvocation对象，让我们来看看这一执行逻辑是怎么控制的：

    public class ReflectiveMethodInvocation implements ProxyMethodInvocation, Cloneable {
     
        protected final Object proxy;
     
        protected final Object target;
     
        protected final Method method;
     
        protected Object[] arguments;
     
        private final Class<?> targetClass;
     
        /**
         * Lazily initialized map of user-specific attributes for this invocation.
         */
        private Map<String, Object> userAttributes;
     
        /**
         * List of MethodInterceptor and InterceptorAndDynamicMethodMatcher
         * that need dynamic checks.
         */
        protected final List<?> interceptorsAndDynamicMethodMatchers;
     
        /**
         * Index from 0 of the current interceptor we're invoking.
         * -1 until we invoke: then the current interceptor.
         */
        private int currentInterceptorIndex = -1;
     
     
        /**
         * Construct a new ReflectiveMethodInvocation with the given arguments.
         * @param proxy the proxy object that the invocation was made on
         * @param target the target object to invoke
         * @param method the method to invoke
         * @param arguments the arguments to invoke the method with
         * @param targetClass the target class, for MethodMatcher invocations
         * @param interceptorsAndDynamicMethodMatchers interceptors that should be applied,
         * along with any InterceptorAndDynamicMethodMatchers that need evaluation at runtime.
         * MethodMatchers included in this struct must already have been found to have matched
         * as far as was possibly statically. Passing an array might be about 10% faster,
         * but would complicate the code. And it would work only for static pointcuts.
         */
        protected ReflectiveMethodInvocation(
                Object proxy, Object target, Method method, Object[] arguments,
                Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
     
            this.proxy = proxy;//proxy对象
            this.target = target;//真实的realSubject对象
            this.targetClass = targetClass;//被代理的类类型
            this.method = BridgeMethodResolver.findBridgedMethod(method);//方法引用
            this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);//调用参数
            this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;//Advice拦截器链
        }
     
     
        @Override
        public final Object getProxy() {
            return this.proxy;
        }
     
        @Override
        public final Object getThis() {
            return this.target;
        }
     
        @Override
        public final AccessibleObject getStaticPart() {
            return this.method;
        }
     
        /**
         * Return the method invoked on the proxied interface.
         * May or may not correspond with a method invoked on an underlying
         * implementation of that interface.
         */
        @Override
        public final Method getMethod() {
            return this.method;
        }
     
        @Override
        public final Object[] getArguments() {
            return (this.arguments != null ? this.arguments : new Object[0]);
        }
     
        @Override
        public void setArguments(Object... arguments) {
            this.arguments = arguments;
        }
     
     
        @Override
        public Object proceed() throws Throwable {
            //	没有拦截器，则直接调用Joinpoint上的method，即直接调用MethodInvocation We start with an index of -1 and increment early.
            if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
                return invokeJoinpoint();
            }
                    // 取得第拦截器链上第N个拦截器 
            Object interceptorOrInterceptionAdvice =
                    this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
            //PointcutInterceptor会走这个逻辑
                    if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
                // Evaluate dynamic method matcher here: static part will already have
                // been evaluated and found to match.
                InterceptorAndDynamicMethodMatcher dm =
                        (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
                //当前拦截器是符合拦截规则，每个拦截器可以定义是否特定的类和方法名是否符合拦截规则
                            //实际上PointCut定义的方法签名最后会转换成这个MethodMatcher，并置于拦截器中
                            if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
                     //符合拦截规则，调用拦截器invoke()	
                                 return dm.interceptor.invoke(this);
                }
                else {
                    // Dynamic matching failed.
                    // Skip this interceptor and invoke the next in the chain.
                                    // 当前方法不需要拦截器操作，则直接往前推进
                                    return proceed();
                }
            }
            else {
                // It's an interceptor, so we just invoke it: The pointcut will have
                // been evaluated statically before this object was constructed.
                            //直接调用拦截器，
                            return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
            }
        }
     
        /**
         * Invoke the joinpoint using reflection.
         * Subclasses can override this to use custom invocation.
         * @return the return value of the joinpoint
         * @throws Throwable if invoking the joinpoint resulted in an exception
         */
        protected Object invokeJoinpoint() throws Throwable {
            return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
        }
上述的代码比较冗杂，解释起来比较繁琐，请看下面一张图，你就知道这段代码的思路了：
<div align="center"> <img src="/images/SpringAOP-2-7.png"/> </div><br>




[实例分析]
根据上面的执行链上的逻辑，我们将我们上面举的例子的输出结果在整理一下：
Advice拦截器的添加顺序:
        proxyFactoryBean.addAdvice(afterReturningAdvice);
        proxyFactoryBean.addAdvice(aroundAdvice);
        proxyFactoryBean.addAdvice(throwsAdvice);
        proxyFactoryBean.addAdvice(beforeAdvice);
##  第一个拦截器：AfterReturningAdvice
第一个添加的是afterReturningAdivce,它所处的位置是第一个拦截器，执行的操作就是：

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object retVal = mi.proceed();
		this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
		return retVal;
	}
也就是说，先完成MethodInvocation的proceed()方法再执行相应的advice；而调用了mi.proceed()方法，导致了当前的调用链后移，进行和后续的操作，
也就是说，AfterReturningAdvice只能等到整个拦截器链上所有执行完毕后才会生效，所以：AFTER_RETURNING:本次服务已结束.... 这句话排在了最后：
<div align="center"> <img src="/images/SpringAOP-2-8.png"/> </div><br>

## 第二个拦截器：AroundAdvice

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("AROUND_ADVICE:BEGIN....");
        Object returnValue = invocation.proceed();
        System.out.println("AROUND_ADVICE:END.....");
        return returnValue;
    }
现在执行到了第二个拦截器，首先输出了"AROUND_ADVICE:BEGIN......"，接着调用Invocation.proceed(),等到剩余的执行完后，再输出"AROUND_ADVICE:END....."：
<div align="center"> <img src="/images/SpringAOP-2-9.png"/> </div><br>

## 第三个拦截器：ThrowsAdvice:
ThrowsAdvice拦截器的处理模式是：

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		try {
		       //先执行invocation.proceed();	
                       return mi.proceed();
		}
		catch (Throwable ex) {
                       //捕捉错误，调用afterThrowing()方法
                        Method handlerMethod = getExceptionHandler(ex);
			if (handlerMethod != null) {
				invokeHandlerMethod(mi, ex, handlerMethod);
			}
			throw ex;
		}
	}

上述的逻辑是，先执行Invocation.proceed();如果这个过程中抛出异常，则调用ThrowsAdvice。
<div align="center"> <img src="/images/SpringAOP-2-10.png"/> </div><br>

## 第四个拦截器:BeforeAdvice:
这个拦截器的工作逻辑如下：

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );//先执行Advice
		return mi.proceed();//后执行Invocation
	}
<div align="center"> <img src="/images/SpringAOP-2-11.png"/> </div><br>

综上所有的拦截器过程，我们就能理解，为什么我们刚开始的输出为什么是下面这样了：
<div align="center"> <img src="/images/SpringAOP-2-12.png"/> </div><br>


# 6、PointCut与Advice的结合------Adivce的条件执行
上面我们提供了几个Adivce，你会发现，这些Advice是无条件地加入了我们创建的对象中。无论调用Target的任何方法，这些Advice都会被触发到。
那么，我们可否告诉Advice，只让它对特定的方法或特定类起作用呢？ 这个实际上是要求我们添加一个过滤器，如果满足条件，则Advice生效，否则无效。
Spring将这个过滤器抽象成如下的接口：

     public interface MethodMatcher {
     
        /**
         * 提供方法签名和所属的Class类型，判断是否支持 
             * Perform static checking whether the given method matches. If this
         * returns {@code false} or if the {@link #isRuntime()} method
         * returns {@code false}, no runtime check (i.e. no.
         * {@link #matches(java.lang.reflect.Method, Class, Object[])} call) will be made.
         * @param method the candidate method
         * @param targetClass the target class (may be {@code null}, in which case
         * the candidate class must be taken to be the method's declaring class)
         * @return whether or not this method matches statically
         */
        boolean matches(Method method, Class<?> targetClass);
     
        /**
         * Is this MethodMatcher dynamic, that is, must a final call be made on the
         * {@link #matches(java.lang.reflect.Method, Class, Object[])} method at
         * runtime even if the 2-arg matches method returns {@code true}?
         * <p>Can be invoked when an AOP proxy is created, and need not be invoked
         * again before each method invocation,
         * @return whether or not a runtime match via the 3-arg
         * {@link #matches(java.lang.reflect.Method, Class, Object[])} method
         * is required if static matching passed
         */
        boolean isRuntime();
     
        /**
         * Check whether there a runtime (dynamic) match for this method,
         * which must have matched statically.
         * <p>This method is invoked only if the 2-arg matches method returns
         * {@code true} for the given method and target class, and if the
         * {@link #isRuntime()} method returns {@code true}. Invoked
         * immediately before potential running of the advice, after any
         * advice earlier in the advice chain has run.
         * @param method the candidate method
         * @param targetClass the target class (may be {@code null}, in which case
         * the candidate class must be taken to be the method's declaring class)
         * @param args arguments to the method
         * @return whether there's a runtime match
         * @see MethodMatcher#matches(Method, Class)
         */
        boolean matches(Method method, Class<?> targetClass, Object... args);
     
     
        /**
         * Canonical instance that matches all methods.
         */
        MethodMatcher TRUE = TrueMethodMatcher.INSTANCE;
     
    }

将这个匹配器MethodMatcher和拦截器Interceptor 结合到一起，就构成了一个新的类InterceptorAndDynamicMethodMatcher ：
    /**
     * Internal framework class, combining a MethodInterceptor instance
     * with a MethodMatcher for use as an element in the advisor chain.
     *
     * @author Rod Johnson
     */
    class InterceptorAndDynamicMethodMatcher {
     
        final MethodInterceptor interceptor;
     
        final MethodMatcher methodMatcher;
     
        public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
            this.interceptor = interceptor;
            this.methodMatcher = methodMatcher;
        }
     
    }
我们再将上述的包含整个拦截器执行链逻辑的ReflectiveMethodInvocation实现的核心代码在过一遍：

	@Override
	public Object proceed() throws Throwable {
		//	We start with an index of -1 and increment early.
		if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
			return invokeJoinpoint();
		}
 
		Object interceptorOrInterceptionAdvice =
				this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		//起到一定的过滤作用，如果不匹配，则直接skip
                if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
			// Evaluate dynamic method matcher here: static part will already have
			// been evaluated and found to match.
			InterceptorAndDynamicMethodMatcher dm =
					(InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
			//满足匹配规则，则拦截器Advice生效
                        if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
				return dm.interceptor.invoke(this);
			}
			else {
				// Dynamic matching failed.
				// Skip this interceptor and invoke the next in the chain.
                                //拦截器尚未生效，直接skip
                                return proceed();
			}
		}
		else {
			// It's an interceptor, so we just invoke it: The pointcut will have
			// been evaluated statically before this object was constructed.
			return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
		}
	}

[实战]
我们现在实现一个PointcutAdisor，PointcutAdvisor表示拥有某个Pointcut的Advisor。

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.aopalliance.aop.Advice;
    import org.springframework.aop.Pointcut;
    import org.springframework.aop.PointcutAdvisor;
     
    /**
     * 实现一个PointcutAdvisor，通过提供的Pointcut,对Advice的执行进行过滤
     * Created by louis on 2016/4/16.
     */
    public class FilteredAdvisor implements PointcutAdvisor {
     
        private Pointcut pointcut;
        private Advice advice;
     
        public FilteredAdvisor(Pointcut pointcut, Advice advice) {
            this.pointcut = pointcut;
            this.advice = advice;
        }
     
        /**
         * Get the Pointcut that drives this advisor.
         */
        @Override
        public Pointcut getPointcut() {
            return pointcut;
        }
     
        @Override
        public Advice getAdvice() {
            return advice;
        }
     
        @Override
        public boolean isPerInstance() {
            return false;
        }
    }

    package org.luanlouis.meditations.thinkinginspring.aop;
     
    import org.aopalliance.aop.Advice;
    import org.springframework.aop.aspectj.AspectJExpressionPointcut;
    import org.springframework.aop.framework.ProxyFactoryBean;
     
    /**
     * 通过ProxyFactoryBean 手动创建 代理对象
     * Created by louis on 2016/4/14.
     */
    public class App {
     
        public static void main(String[] args) throws Exception {
     
            //1.针对不同的时期类型，提供不同的Advice
            Advice beforeAdvice = new TicketServiceBeforeAdvice();
            Advice afterReturningAdvice = new TicketServiceAfterReturningAdvice();
            Advice aroundAdvice = new TicketServiceAroundAdvice();
            Advice throwsAdvice = new TicketServiceThrowsAdvice();
     
            RailwayStation railwayStation = new RailwayStation();
     
            //2.创建ProxyFactoryBean,用以创建指定对象的Proxy对象
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
           //3.设置Proxy的接口
            proxyFactoryBean.setInterfaces(TicketService.class);
            //4. 设置RealSubject
            proxyFactoryBean.setTarget(railwayStation);
            //5.使用JDK基于接口实现机制的动态代理生成Proxy代理对象，如果想使用CGLIB，需要将这个flag设置成true
            proxyFactoryBean.setProxyTargetClass(true);
     
            //5. 添加不同的Advice
     
            proxyFactoryBean.addAdvice(afterReturningAdvice);
            proxyFactoryBean.addAdvice(aroundAdvice);
            proxyFactoryBean.addAdvice(throwsAdvice);
            //proxyFactoryBean.addAdvice(beforeAdvice);
            proxyFactoryBean.setProxyTargetClass(false);
     
            //手动创建一个pointcut,专门拦截sellTicket方法
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution( * sellTicket(..))");
            //传入创建的beforeAdvice和pointcut
            FilteredAdvisor sellBeforeAdvior = new FilteredAdvisor(pointcut,beforeAdvice);
            //添加到FactoryBean中
            proxyFactoryBean.addAdvisor(sellBeforeAdvior);
            
            //通过ProxyFactoryBean生成
            TicketService ticketService = (TicketService) proxyFactoryBean.getObject();
            ticketService.sellTicket();
            System.out.println("---------------------------");
            ticketService.inquire();
     
        }
     
     
    }

这个时候，你会看到输出：
<div align="center"> <img src="/images/SpringAOP-2-13.png"/> </div><br>

从结果中你可以清晰地看到，我们可以对某一个Advisor(即Advice)添加一个pointcut限制，这样就可以针对指定的方法执行Advice了！
本例中使用了PointcutAdvisor,实际上，带底层代码中，Spring会将PointcutAdvisor转换成InterceptorAndDynamicMethodMatcher 
参与ReflectiveMethodInvocation关于拦截器链的执行逻辑：

    public class DefaultAdvisorChainFactory implements AdvisorChainFactory, Serializable {
     
        @Override
        public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
                Advised config, Method method, Class<?> targetClass) {
     
            // This is somewhat tricky... We have to process introductions first,
            // but we need to preserve order in the ultimate list.
            List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors().length);
            Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
            boolean hasIntroductions = hasMatchingIntroductions(config, actualClass);
            AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
                    
            for (Advisor advisor : config.getAdvisors()) {
                                    //PointcutAdvisor向 InterceptorAndDynamicMethodMatcher 的转换  
                                   if (advisor instanceof PointcutAdvisor) {
                    // Add it conditionally.
                    PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                    if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                        MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                        MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                        if (MethodMatchers.matches(mm, method, actualClass, hasIntroductions)) {
                            if (mm.isRuntime()) {
                                // Creating a new object instance in the getInterceptors() method
                                // isn't a problem as we normally cache created chains.
                                for (MethodInterceptor interceptor : interceptors) {
                                    interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
                                }
                            }
                            else {
                                interceptorList.addAll(Arrays.asList(interceptors));
                            }
                        }
                    }
                }
                else if (advisor instanceof IntroductionAdvisor) {
                    IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
                    if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
                        Interceptor[] interceptors = registry.getInterceptors(advisor);
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
                else {
                    Interceptor[] interceptors = registry.getInterceptors(advisor);
                    interceptorList.addAll(Arrays.asList(interceptors));
                }
            }
     
            return interceptorList;
        }
     
        /**
         * Determine whether the Advisors contain matching introductions.
         */
        private static boolean hasMatchingIntroductions(Advised config, Class<?> actualClass) {
            for (int i = 0; i < config.getAdvisors().length; i++) {
                Advisor advisor = config.getAdvisors()[i];
                if (advisor instanceof IntroductionAdvisor) {
                    IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
                    if (ia.getClassFilter().matches(actualClass)) {
                        return true;
                    }
                }
            }
            return false;
        }
     
    }


# 7、总结
至此，你已经了解了Spring的AOP的精髓，以及Spring的整个工作机制。我个人认为,想要理解Spring AOP，你需要从ProxyFactoryBean 开始，
逐步地分析整个代理的构建过程：
1. 代理对象是怎么生成的(JDK or Cglib)
2. Advice链(即拦截器链)的构造过程以及执行机制
3. 如何在Advice上添加pointcut,并且这个pointcut是如何工作的(实际上起到的过滤作用)
最后再讲一下性能问题，如上面描述的，Spring创建Proxy的过程逻辑虽然很清晰，但是你也看到，对于我们每一个方法调用，
都会经过非常复杂的层层Advice拦截判断，是否需要拦截处理，这个开销是非常大的。记得Spring的documentation介绍，
如果使用Spring的AOP，对项目而言会造成10%的性能消耗，So，用AOP之前要仔细考虑一下性能问题~~~~~

