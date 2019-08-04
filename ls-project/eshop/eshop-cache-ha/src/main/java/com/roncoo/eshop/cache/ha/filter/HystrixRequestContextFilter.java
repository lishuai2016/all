package com.roncoo.eshop.cache.ha.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * hystrix请求上下文过滤器
 * @author Administrator
 *
 */
public class HystrixRequestContextFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		try {
			chain.doFilter(request, response); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.shutdown();
		}
	}

	public void destroy() {
		
	}

}
