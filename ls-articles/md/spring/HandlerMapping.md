---
title: HandlerMapping
categories: 
- spring
tags:
---


# HandlerMapping


---HandlerMapping接口
	---MatchableHandlerMapping接口
		---RequestMappingHandlerMapping类
		---AbstractUrlHandlerMapping抽象类（**********************************关键）
			---AbstractDetectingUrlHandlerMapping抽象类
				---BeanNameUrlHandlerMapping类（重要：默认）
			---WelcomePageHandlerMapping final类
			---SimpleUrlHandlerMapping类（重要）
				---WebSocketHandlerMapping类
	---AbstractHandlerMapping类
		---AbstractUrlHandlerMapping
			--- 。。。同上面的
		---AbstractHandlerMethodMapping抽象类
			---RequestMappingInfoHandlerMapping抽象类




## 1、HandlerMapping接口
public interface HandlerMapping {

//返回一个包含处理器对象和拦截器栈的HandlerExecutionChain，具体的映射机制由实现类来确定
//Return a handler and any interceptors for this request. The choice may be made on request URL, session state, or any factor the implementing class chooses.
	@Nullable
	HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
	...
}


可以大致这样做个分类:
- 1. 一个接口HandlerMapping,定义一个api: HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
- 2. 一个基础抽象类:主要是准备上下文环境,提供getHandlerInternal钩子,封装拦截器到HandlerExecutionChain
- 3. 基于注解@Controller,@RequestMapping的使用
- 4. 配置文件中直接配置url到 handler的SimpleUrlHandlerMapping
- 5. 默认实现BeanNameUrlHandlerMapping
- 6. Controller子类的映射



### 1.1、MatchableHandlerMapping接口
```java
public interface MatchableHandlerMapping extends HandlerMapping {

	@Nullable
	RequestMatchResult match(HttpServletRequest request, String pattern);

}
```

### 1.2、AbstractHandlerMapping抽象类
```java
public abstract class AbstractHandlerMapping extends WebApplicationObjectSupport implements HandlerMapping, Ordered, BeanNameAware {

	@Nullable
	private Object defaultHandler;

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	private PathMatcher pathMatcher = new AntPathMatcher();

	private final List<Object> interceptors = new ArrayList<>();

	private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();

	private CorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

	private CorsProcessor corsProcessor = new DefaultCorsProcessor();

	private int order = Ordered.LOWEST_PRECEDENCE;  // default: same as non-Ordered

	@Nullable
	private String beanName;

	@Override
	@Nullable
	public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		Object handler = getHandlerInternal(request);  //钩子函数，由子类来实现具体的处理handler
		if (handler == null) {
			handler = getDefaultHandler();
		}
		if (handler == null) {
			return null;
		}
		// Bean name or resolved handler?
		if (handler instanceof String) {
			String handlerName = (String) handler;
			handler = obtainApplicationContext().getBean(handlerName);  //根据name去spring容器获得一个对象handler
		}

		//根据handler构建HandlerExecutionChain对象
		HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

		if (CorsUtils.isCorsRequest(request)) {
			CorsConfiguration globalConfig = this.corsConfigurationSource.getCorsConfiguration(request);
			CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
			CorsConfiguration config = (globalConfig != null ? globalConfig.combine(handlerConfig) : handlerConfig);
			executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
		}

		return executionChain;
	}

	//抽象的钩子函数，子类必须实现
	@Nullable
	protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;


	//根据handler构建HandlerExecutionChain对象（主要是添加拦截器）
	protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
		HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
				(HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
			if (interceptor instanceof MappedInterceptor) {
				MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;   //根据URL规则添加拦截器（这个MappedInterceptor的优先级高？？？？）
				if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
					chain.addInterceptor(mappedInterceptor.getInterceptor());
				}
			}
			else {
				chain.addInterceptor(interceptor);
			}
		}
		return chain;
	}



}
```



参考：
https://www.cnblogs.com/leftthen/p/5210932.html