package com.ls.distributed.sequence;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-02-20 10:15
参考：
https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247484640&idx=1&sn=375e916c89089e4af96ead963036d015&chksm=fba6ece3ccd165f5aedbfbdd501c2702417182facf47166b961e5ec418173a32433aa4b44d1d&scene=0#rd

snowflake算法，是twitter开源的分布式id生成算法。

其核心思想就是：使用一个64 bit的long型的数字作为全局唯一id，这64个bit中，其中1个bit是不用的，然后用其中的41 bit作为毫秒数，
用10 bit作为工作机器id，12 bit作为序列号。

给大家举个例子吧，比如下面那个64 bit的long型数字，大家看看

0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000

上面第一个部分，是1个bit：0，这个是无意义的
上面第二个部分是41个bit：表示的是时间戳
上面第三个部分是5个bit：表示的是机房id，10001
上面第四个部分是5个bit：表示的是机器id，1 1001
上面第五个部分是12个bit：表示的序号，就是某个机房某台机器上这一毫秒内同时生成的id的序号，0000 00000000


1 bit：是不用的，为啥呢？
因为二进制里第一个bit为如果是1，那么都是负数，但是我们生成的id都是正数，所以第一个bit统一都是0

41 bit：表示的是时间戳，单位是毫秒。
41 bit可以表示的数字多达2^41 - 1，也就是可以标识2 ^ 41 - 1个毫秒值，换算成年就是表示69年的时间。

10 bit：记录工作机器id，代表的是这个服务最多可以部署在2^10台机器上，也就是1024台机器。
但是10 bit里5个bit代表机房id，5个bit代表机器id。意思就是最多代表2 ^ 5个机房（32个机房），每个机房里可以代表2 ^ 5个机器（32台机器）。

12 bit：这个是用来记录同一个毫秒内产生的不同id。
12 bit可以代表的最大正整数是2 ^ 12 - 1 = 4096，也就是说可以用这个12bit代表的数字来区分同一个毫秒内的4096个不同的id


简单来说，你的某个服务假设要生成一个全局唯一id，那么就可以发送一个请求给部署了snowflake算法的系统，由这个snowflake算法系统来生成唯一id。
这个snowflake算法系统首先肯定是知道自己所在的机房和机器的，比如机房id = 17，机器id = 12。
接着snowflake算法系统接收到这个请求之后，首先就会用二进制位运算的方式生成一个64 bit的long型id，64个bit中的第一个bit是无意义的。
接着41个bit，就可以用当前时间戳（单位到毫秒），然后接着5个bit设置上这个机房id，还有5个bit设置上机器id。
最后再判断一下，当前这台机房的这台机器上这一毫秒内，这是第几个请求，给这次生成id的请求累加一个序号，作为最后的12个bit。
 */
public class IdWorker {

    private long workerId; // 这个就是代表了机器id
    private long datacenterId; // 这个就是代表了机房id
    private long sequence; // 这个就是代表了一毫秒内生成的多个id的最新序号

    public IdWorker(long workerId, long datacenterId, long sequence) {

        // sanity check for workerId
        // 这儿不就检查了一下，要求就是你传递进来的机房id和机器id不能超过32，不能小于0
        if (workerId > maxWorkerId || workerId < 0) {

            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
        }

        if (datacenterId > maxDatacenterId || datacenterId < 0) {

            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    private long twepoch = 1288834974657L;

    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;

    // 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 这个是一个意思，就是5 bit最多只能有31个数字，机房id最多只能是32以内
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public long getWorkerId(){
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    // 这个是核心方法，通过调用nextId()方法，让当前这台机器上的snowflake算法程序生成一个全局唯一的id
    public synchronized long nextId() {

        // 这儿就是获取当前时间戳，单位是毫秒
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.err.printf(
                    "clock is moving backwards. Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }


        // 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
        // 这个时候就得把seqence序号给递增1，最多就是4096
        if (lastTimestamp == timestamp) {

            // 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
            //这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & sequenceMask;

            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }

        } else {
            sequence = 0;
        }

        // 这儿记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;

        // 这儿就是最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {

        long timestamp = timeGen();

        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen(){
        return System.currentTimeMillis();
    }

    //---------------测试---------------
    public static void main(String[] args) {

        IdWorker worker = new IdWorker(1,1,1);

        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId());
        }
    }
}
