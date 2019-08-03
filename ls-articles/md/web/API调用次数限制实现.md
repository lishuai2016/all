[API 调用次数限制实现](https://zhuanlan.zhihu.com/p/20872901)

API 调用次数限制实现


不错的分析文章

我们可以在 Bucket 中存放现在的 Token 数量，然后存储上一次补充 Token 的时间戳，当用户下一次请求获取一个 Token 的时候， 根据此时的时间戳，计算从上一个时间戳开始，到现在的这个时间点所补充的所有 Token 数量，加入到 Bucket 当中。

这种实现方法有几个优势：

- 首先, 避免了给每一个 Bucket 设置一个定时器这种笨办法，
- 第二，数据结构需要的内存量很小，只需要储存 Bucket 中剩余的 Token 量以及上次补充 Token 的时间戳就可以了；
- 第三，只有在用户访问的时候，才会计算 Token 补充量，对于系统的计算资源占用量也较小。

确定和实现方法之后，就可以开始实现这个算法了。首先要考虑的是 Bucket 存放在哪里？虽然 Bucket 占用内存的数量 很小，假设一个 Bucket 的大小为 20 个字节，如果需要储存一百万个 Bucket 就需要使用 20M 的内存。而且，Bucket 从 一定意义上属于缓存数据，因为如果用户长期不使用这个 Bucket 的话，应该能够自动失效。从上面的分析，自然地，我想到 将 Bucket 放在 Redis 当中，每个 Bucket 使用一个 Hash 存放（HSET）， 并且支持在一段时间之后，使 Bucket 失效（TTL）。

下面的方法是最初的实现方法，对于每一个 Token Bucket，在 Redis 上面，使用一个 Hash 进行表示，一个 Token Bucket 有 lastRefillTime 表示最后一次补充 Token 的时间，tokensRemaining 则表示 Bucket 中的剩余 Token 数量，access() 方法大致的步骤为：

- 当一个请求 Token进入 access() 方法后，先计算计算该请求的 Token Bucket 的 key；
- 如果这个 Token Bucket 在 Redis 中不存在，那么就新建一个 Token Bucket，然后设置该 Bucket 的 Token 数量为最大值减一(去掉了这次请求获取的 Token）。 在初始化 Token Bucket 的时候将 Token 数量设置为最大值这一点在后面还有讨论；
- 如果这个 Token Bucket 在 Redis 中存在，而且其上一次加入 Token 的时间到现在时间的时间间隔大于 Token Bucket 的 interval，那么也将 Bucket 的 Token 值重置为最大值减一；
- 如果 Token Bucket 上次加入 Token 的时间到现在时间的时间间隔没有大于 interval，那么就计算这次需要补充的 Token 数量，将补充过后的 Token 数量更新到 Token Bucket 中。



package com.yichao.woo.ratelimiter;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
* Created by Yichao-Woo.
*/
public class RateLimiter {

    private JedisPool jedisPool;

    private long intervalInMills;
    private long limit;
    private double intervalPerPermit;

    public RateLimiter() {
        jedisPool = new JedisPool("192.168.38.3", 6379);
        intervalInMills = 10000;
        limit = 3;
        intervalPerPermit = intervalInMills * 1.0 / limit;
    }

    // 单线程操作下才能保证正确性
    // 需要这些操作原子性的话，最好使用 redis 的 lua script
    public boolean access(String userId) {

        String key = genKey(userId);

        try (Jedis jedis = jedisPool.getResource()) {
//散列key的所有键值对为
            Map<String, String> counter = jedis.hgetAll(key);//获取一个map的引用

            if (counter.size() == 0) {
                TokenBucket tokenBucket = new TokenBucket(System.currentTimeMillis(), limit - 1);
                jedis.hmset(key, tokenBucket.toHash());//(key,map)
                return true;
            } else {
                TokenBucket tokenBucket = TokenBucket.fromHash(counter);

                long lastRefillTime = tokenBucket.getLastRefillTime();
                long refillTime = System.currentTimeMillis();
                long intervalSinceLast = refillTime - lastRefillTime;

                long currentTokensRemaining;
                if (intervalSinceLast > intervalInMills) {
                    currentTokensRemaining = limit;
                } else {
                    long grantedTokens = (long) (intervalSinceLast / intervalPerPermit);
                    System.out.println(grantedTokens);
                    currentTokensRemaining = Math.min(grantedTokens + tokenBucket.getTokensRemaining(), limit);
                }

                tokenBucket.setLastRefillTime(refillTime);
                assert currentTokensRemaining >= 0;
                if (currentTokensRemaining == 0) {
                    tokenBucket.setTokensRemaining(currentTokensRemaining);
                    jedis.hmset(key, tokenBucket.toHash());
                    return false;
                } else {
                    tokenBucket.setTokensRemaining(currentTokensRemaining - 1);
                    jedis.hmset(key, tokenBucket.toHash());
                    return true;
                }
            }
        }
    }

    private String genKey(String userId) {
        return "rate:limiter:" + intervalInMills + ":" + limit + ":" + userId;
    }

    @Getter
    @Setter
    public static class TokenBucket {
        private long lastRefillTime;
        private long tokensRemaining;

        public TokenBucket(long lastRefillTime, long tokensRemaining) {
            this.lastRefillTime = lastRefillTime;
            this.tokensRemaining = tokensRemaining;
        }

        public static TokenBucket fromHash(Map<String, String> hash) {
            long lastRefillTime = Long.parseLong(hash.get("lastRefillTime"));
            int tokensRemaining = Integer.parseInt(hash.get("tokensRemaining"));
            return new TokenBucket(lastRefillTime, tokensRemaining);
        }

        public Map<String, String> toHash() {
            Map<String, String> hash = new HashMap<>();
            hash.put("lastRefillTime", String.valueOf(lastRefillTime));
            hash.put("tokensRemaining", String.valueOf(tokensRemaining));
            return hash;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter();

        for (int i = 0; i < 3; i++) {
            boolean yigwoo = rateLimiter.access("yigwoo");
            System.out.println(yigwoo);
        }

        boolean yigwoo = rateLimiter.access("yigwoo");
        System.out.println(yigwoo);

        Thread.sleep(7000);

        boolean yigwoo1 = rateLimiter.access("yigwoo");
        System.out.println(yigwoo1);
    }
}
