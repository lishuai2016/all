package com.ls.lishuai.xianliu;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/3 10:25
 *
 *
令牌桶算法比漏桶算法稍显复杂。首先，我们有一个固定容量的桶，桶里存放着令牌（token）。
桶一开始是空的，token以一个固定的速率r往桶里填充，直到达到桶的容量，多余的令牌将会被丢弃。
每当一个请求过来时，就会尝试从桶里移除一个令牌，如果没有令牌的话，请求无法通过。
 */
public class TokenBucketDemo {

    public long timeStamp = System.currentTimeMillis();
    public long capacity; // 桶的容量
    public long rate; // 令牌放入速度
    public long tokens; // 当前令牌数量
    public boolean grant() {
        long now = System.currentTimeMillis();
        // 先添加令牌
        tokens = Math.min(capacity, tokens + (now - timeStamp) * rate);
        timeStamp = now;
        if (tokens < 1) {
            // 若不到1个令牌,则拒绝
            return false;
        }
        else {
            // 还有令牌，领取令牌
            tokens -= 1;
            return true;
        }
    }
}
