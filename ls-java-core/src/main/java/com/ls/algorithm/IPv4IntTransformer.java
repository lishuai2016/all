package com.ls.algorithm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 问题分析
 对于ipv4的地址来说，如果用字符串的形式存储的话，其占用字节就比较大，比如对于IPv4地址0.0.0.0的字符串，就需要7个字节，IPv4为255.255.255.255 的字符串，需要15个字节，也就是说存储一个ip需要占用7~15个字节。
 那么有没有更节省空间的存储方式呢？答案是有。
 方案1： 直接把字符串中的'.'去掉，不就变成一个数字了嘛，比如 "255.255.255.255" 变成 255255255255，然而我们知道int所能表示的最大值 = Integer.MAX_VALUE = 2^31-1 = 2147483647， 255255255255 > 2^31-1，所以需要用长整形long来表示，长整形占用8个字节，也就是说我们将7~15个字节转换为8字节，在绝大多数情况下是节省空间了的。
 方案2： 因为考虑到IPv4的地址本质上就是32位的二进制串，而一个int类型的数字刚好为4个字节32个bit位，所以刚好可以用一个int类型的数字转表示IPv4地址。所以，我们可以用4个字节的int数字表示一个ip地址，这样可以大大节省空间。
 这里只讨论方案2 :)
 演示
 对于ipv4地址： 192.168.1.3：
 每段都用二进制表示： 192(10) = 11000000(2) ; 168(10) = 10101000(2) ; 1(10) = 00000001(2) ;  3(10) = 00000011(2) 。
 所以连在一起就是：11000000101010000000000100000011，对应的int数字就是-1062731775 。
 具体算法分析：
 192左移24位： 11000000 00000000 00000000 00000000
 168左移16位： 00000000 10101000 00000000 00000000
 001左移08位： 00000000 00000000 00000001 00000000
 003左移00位： 00000000 00000000 00000000 00000011
 按位或结果   ： 11000000 10101000  00000001 00000011
 即 -1062731775
 将int类型的数字转换成ip地址，其实就是上述过程的逆过程，这里就不再赘述。


https://www.jianshu.com/p/70185244e46c

 */

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-01-10 10:47
 * IPv4地址和int数字的互相转换
 */
public class IPv4IntTransformer {

    /**
     * IPv4地址转换为int类型数字
     *
     */
    public static int ip2Integer(String ipv4Addr) {
        // 判断是否是ip格式的
        if (!isIPv4Address(ipv4Addr))
            throw new RuntimeException("Invalid ip address");

        // 匹配数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(ipv4Addr);
        int result = 0;
        int counter = 0;
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group());
            result = (value << 8 * (3 - counter++)) | result;
        }
        return result;
    }

    /**
     * 判断是否为ipv4地址
     *
     */
    private static boolean isIPv4Address(String ipv4Addr) {
        String lower = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])"; // 0-255的数字
        String regex = lower + "(\\." + lower + "){3}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipv4Addr);
        return matcher.matches();
    }

    /**
     * 将int数字转换成ipv4地址
     *
     */
    public static String integer2Ip(int ip) {
        StringBuilder sb = new StringBuilder();
        int num = 0;
        boolean needPoint = false; // 是否需要加入'.'
        for (int i = 0; i < 4; i++) {
            if (needPoint) {
                sb.append('.');
            }
            needPoint = true;
            int offset = 8 * (3 - i);
            num = (ip >> offset) & 0xff;
            sb.append(num);
        }
        return sb.toString();
    }
}

