---
title: Markdown语法总结
categories: 
- spring
tags:
---

# HandlerInterceptor



## 0、HandlerInterceptor拦截器接口
```java
public interface HandlerInterceptor {

//Called after HandlerMapping determined an appropriate handler object, but before HandlerAdapter invokes the handler.
// 在controller中的方法执行之前执行
default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}


//Called after HandlerAdapter actually invoked the handler, but before the DispatcherServlet renders the view.Can expose additional model objects to the view via the given ModelAndView.
//在controller中的映射方法处理完之后，并且还没有做视图的渲染的时候执行，此外，可以修改ModelAndView对象
default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}


 //Callback after completion of request processing, that is, after rendering the view. Will be called on any outcome of handler execution, thus allows for proper resource cleanup.
//在视图view渲染后执行，做一些资源的清理工作
default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}

}
```



##1、MappedInterceptor该类不可以再被继承（是用来判断请求符不符合拦截的条件）
（装饰器设计模式：实现共同的接口，包含，并且包含被装饰者的实例，通过构造函数导入）

```java
public final class MappedInterceptor implements HandlerInterceptor {

	@Nullable
	private final String[] includePatterns; //拦截器需要拦截的路径

	@Nullable
	private final String[] excludePatterns; //拦截器不需要拦截的路径

	private final HandlerInterceptor interceptor; //拦截器处理对象

	@Nullable
	private PathMatcher pathMatcher;

	//核心的构造函数，包含几个重载的构造函数，但是必须的参数需要有includePatterns和拦截器实例interceptor
	public MappedInterceptor(@Nullable String[] includePatterns, @Nullable String[] excludePatterns,
			HandlerInterceptor interceptor) {

		this.includePatterns = includePatterns;
		this.excludePatterns = excludePatterns;
		this.interceptor = interceptor;
	}

	//给的的路径是否满足指定的匹配策略
	public boolean matches(String lookupPath, PathMatcher pathMatcher) {
		PathMatcher pathMatcherToUse = (this.pathMatcher != null ? this.pathMatcher : pathMatcher);
		if (!ObjectUtils.isEmpty(this.excludePatterns)) {
			for (String pattern : this.excludePatterns) {
				if (pathMatcherToUse.match(pattern, lookupPath)) {   //不需要拦截的
					return false;
				}
			}
		}
		if (ObjectUtils.isEmpty(this.includePatterns)) {
			return true;
		}
		for (String pattern : this.includePatterns) {
			if (pathMatcherToUse.match(pattern, lookupPath)) {  //需要进行拦截的
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return this.interceptor.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {

		this.interceptor.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {

		this.interceptor.afterCompletion(request, response, handler, ex);
	}

//...
}
```






