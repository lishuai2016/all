---
title: 分布式系统的BASE理论
categories: 
- 分布式
tags:
---


分布式系统的BASE理论[最终一致性（Eventual Consitency）]


[参考](http://www.hollischuang.com/archives/672)


# BASE理论
eBay的架构师Dan Pritchett源于对大规模分布式系统的实践总结，在ACM上发表文章提出BASE理论，[BASE理论是对CAP理论的延伸]，
核心思想是即使无法做到强一致性（Strong Consistency，CAP的一致性就是强一致性），
但应用可以采用适合的方式达到[最终一致性（Eventual Consitency）]。

[BASE是指基本可用（Basically Available）、软状态（ Soft State）、最终一致性（ Eventual Consistency）]

## 1、基本可用（Basically Available）
基本可用是指分布式系统在出现故障的时候，允许损失部分可用性，即保证核心可用。
电商大促时，为了应对访问量激增，部分用户可能会被引导到降级页面，服务层也可能只提供降级服务。这就是损失部分可用性的体现。

## 2、软状态（ Soft State）
软状态是指允许系统存在中间状态，而该中间状态不会影响系统整体可用性。分布式存储中一般一份数据至少会有三个副本，
允许不同节点间副本同步的延时就是软状态的体现。mysql replication的异步复制也是一种体现。

## 3、最终一致性（ Eventual Consistency）
最终一致性是指系统中的所有数据副本经过一定时间后，最终能够达到一致的状态。弱一致性和强一致性相反，最终一致性是弱一致性的一种特殊情况。

## ACID和BASE的区别与联系
ACID是传统数据库常用的设计理念，追求强一致性模型。
BASE支持的是大型分布式系统，提出通过牺牲强一致性获得高可用性。
ACID和BASE代表了两种截然相反的设计哲学
在分布式系统设计的场景中，系统组件对一致性要求是不同的，因此ACID和BASE又会结合使用。
