package com.ls.lishuai.ip;


import com.ls.lishuai.ip.util.IpHelper;

/**
 * @Author: lishuai
 * @CreateDate: 2018/11/25 12:21
 */
public class Main {
    public static void main(String[] args) {
        String ip = "58.30.15.255";
        String region = IpHelper.findRegionByIp(ip);
        System.out.println(region);
    }
}
