package com.ls.io.bupt.io;

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
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));   //根据socket构建输入流
            out = new PrintWriter(socket.getOutputStream(),true); //根据socket构建输出流

            out.println("QUERY TIME ORDER");    //往输出流中写数据，发送请求
            System.out.println("send order 2 server succeed.");

            String res = in.readLine();//从输入流读取响应
            System.out.println("now is:" + res);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
