---
title: Spring Cloud相关组件
categories: 
- Markdown
tags:
---

# Spring Cloud相关组件

## 1、Eureka
各个服务启动时，Eureka Client都会将服务注册到Eureka Server，
并且Eureka Client还可以反过来从Eureka Server拉取注册表，从而知道其他服务在哪里

## 2、Ribbon
服务间发起请求的时候，基于Ribbon做负载均衡，从一个服务的多台机器中选择一台

## 3、Feign
基于Feign的动态代理机制，根据注解和选择的机器，拼接请求URL地址，发起请求（消费端默认使用ribbon进行负载均衡）

## 4、Hystrix
发起请求是通过Hystrix的线程池来走的，不同的服务走不同的线程池，实现了不同服务调用的隔离，避免了服务雪崩的问题

## 5、Zuul
如果前端、移动端要调用后端系统，统一从Zuul网关进入，由Zuul网关转发请求给对应的服务


## 6、consul
注册中心

## 7configserver
配置中心

## 8、sleuth
分布式链路追踪

## 9、bus
消息总线

## 10、dashboard
实时监控仪表盘

## 11、service mesh












