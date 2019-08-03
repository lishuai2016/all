package com.ls.util;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-09
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: db-parser-component
 * @author: lishuai
 * @create: 2018-12-05
 * 获取运行此程序的机器IP
 */
public class IPGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(IPGenerator.class);
    static final int ip;
    static AtomicInteger id = new AtomicInteger(0);

    static {
        int tmpIp = -1;
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    LOG.debug("check ip "+inetAddress.getHostAddress());
                    if (inetAddress.getAddress().length == 4) {
                        int first = (inetAddress.getAddress()[0] & 0xFF);
                        if (first != 127 && first != 192) {
                            tmpIp = (inetAddress.getAddress()[3] & 0xFF);
                            break;
                        }
                    }
                }
                if (tmpIp != -1) {
                    break;
                }
            }
            ip = tmpIp;
        } catch (SocketException e) {
            throw new RuntimeException("can't get valid ip");
        }
    }

    public static int genId() {
        return id.incrementAndGet();
    }

    public static int getIntFormatIp() {
        return ip;
    }

    public static void main(String[] args) {
        System.out.println(getIntFormatIp());
    }
}

