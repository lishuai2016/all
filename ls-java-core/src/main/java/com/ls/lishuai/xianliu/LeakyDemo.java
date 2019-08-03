package com.ls.lishuai.xianliu;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/3 10:23
从图中我们可以看到，整个算法其实十分简单。
首先，我们有一个固定容量的桶，有水流进来，也有水流出去。对于流进来的水来说，我们无法预计一共有多少水会流进来，
也无法预计水流的速度。但是对于流出去的水来说，这个桶可以固定水流出的速率。而且，当桶满了之后，多余的水将会溢出。

我们将算法中的水换成实际应用中的请求，我们可以看到漏桶算法天生就限制了请求的速度。
当使用了漏桶算法，我们可以保证接口会以一个常速速率来处理请求。所以漏桶算法天生不会出现临界问题。具体的伪代码实现如下：
 */
public class LeakyDemo {
    public long timeStamp = System.currentTimeMillis();
    public long capacity; // 桶的容量
    public long rate; // 水漏出的速度
    public long water; // 当前水量(当前累积请求数)
    public boolean grant() {
        long now = System.currentTimeMillis();
        water = Math.max(0, water - (now - timeStamp) * rate); // 先执行漏水，计算剩余水量
        timeStamp = now;
        if ((water + 1) < capacity) {
            // 尝试加水,并且水还未满
            water += 1;
            return true;
        } else {
            // 水满，拒绝加水
            return false;
        }
    }
}
