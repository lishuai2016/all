package com.ls.io.bupt.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lishuai on 2017/6/17.
 */
public class TimeClient {
    public static void main(String[] ars) {
        int port = 8080;
       new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClientHandle-001").start();
    }
}
