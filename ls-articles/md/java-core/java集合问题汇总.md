[源码阅读之ArrayList](https://mp.weixin.qq.com/s?__biz=MzI0NjUxNTY5Nw==&mid=2247483699&idx=1&sn=1a11f45640e7e917a42c0751a0ed20c3&scene=19&key=990de978484973e69ae67a81832648a291a6a37125f4fd5bb625c4bfc3eb66007274bf3516cf3d1b0e5d5dab8a047d3124725ca9b8d2080e1755e01408bbbe3c12e0f0d3fc0e21c9c3c186d50019cbb5&ascene=7&uin=MjI3MDUzNjkwMw%3D%3D&devicetype=Windows+10&version=6203005d&pass_ticket=3wxHr4gxuhKloKpaB1Bamf15b8Hq5b%2FR2L55Y4WK3CdKShJkM5n8kyaK%2FPHd1zn8&winzoom=1)
[聊聊并发-Java中的Copy-On-Write容器](http://ifeve.com/java-copy-on-write/)
[源码阅读之Vector](https://www.jianshu.com/p/3a6d5fc44122)

[Java集合框架的知识总结（1）](http://www.cnblogs.com/zhxxcq/archive/2012/03/11/2389611.html)

[PriorityQueue](https://blog.csdn.net/hudashi/article/details/6942789)




# PriorityQueue
PriorityQueue(用数组实现，维护一个二叉树的结构，初始11， 下标为i的元素的父节点为queue[(i-1)/2])，默认最小元素在队列的头部

子节点i的父节点：（i - 1）/2
父节点k的左节点：k/2 + 1，右节点k/2 + 2


PriorityQueue对元素采用的是堆排序，头是按指定排序方式的最小元素。堆排序只能保证根是最大（最小），整个堆并不是有序的。
方法iterator()中提供的迭代器可能只是对整个数组的依次遍历。也就只能保证数组的第一个元素是最小的。

queue这样的二叉树结构有一个特性，即如果数组的长度为length，那么所有下标大于length/2的节点都是叶子节点，其它的节点都有子节点。
总结：可以看到这种算法的实现，充分利用了树结构在搜索操作时的优势，效率又高于维护一个全部有序的队列。









