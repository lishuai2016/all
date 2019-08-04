package com.zhss.demo.zuul.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 定时刷新任务
 */
@Component
@Configuration
@EnableScheduling
public class RefreshRouteTask {

	@Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteLocator routeLocator;

    /**
     * 定时发布路由刷新事件，然后最后会调用com.zhss.demo.zuul.gateway.DynamicRouteLocator#locateRoutes()
     * 来实现刷新路由信息
     *
     * 如何实现的？在哪里处理的这个监听事件
     */
    @Scheduled(fixedRate = 5000)
    private void refreshRoute() {
        System.out.println("定时刷新路由表");
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}