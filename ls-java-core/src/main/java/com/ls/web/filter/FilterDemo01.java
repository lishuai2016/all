package com.ls.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-08 10:36
 */
public class FilterDemo01 implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("----过滤器初始化----");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                             FilterChain chain) throws IOException, ServletException {

            //对request和response进行一些预处理
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            System.out.println("FilterDemo01执行前！！！");
            chain.doFilter(request, response);  //让目标资源执行，放行
            System.out.println("FilterDemo01执行后！！！");
        }

        @Override
        public void destroy() {
            System.out.println("----过滤器销毁----");
        }
}
