---
title: JMH(Java Microbenchmark Harness)
categories: 
- JMH
tags:
---


JMH，即Java Microbenchmark Harness，是专门用于代码微基准测试的工具套件。
何谓Micro Benchmark呢？简单的来说就是基于方法层面的基准测试，精度可以达到微秒级。
当你定位到热点方法，希望进一步优化方法性能的时候，就可以使用JMH对优化的结果进行量化的分析。
和其他竞品相比——如果有的话，JMH最有特色的地方就是，它是由Oracle内部实现JIT的那拨人开发的，
对于JIT以及JVM所谓的“profile guided optimization”对基准测试准确性的影响可谓心知肚明(smile)

JMH比较典型的应用场景有：
想准确的知道某个方法需要执行多长时间，以及执行时间和输入之间的相关性；
对比接口不同实现在给定条件下的吞吐量；
查看多少百分比的请求在多长时间内完成；


https://blog.csdn.net/lxbjkben/article/details/79410740