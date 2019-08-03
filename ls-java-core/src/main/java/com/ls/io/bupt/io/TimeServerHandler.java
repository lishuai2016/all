package com.ls.io.bupt.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by lishuai on 2017/6/17.
 */
public class TimeServerHandler implements  Runnable{
    private Socket socket;
    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));  //根据socket构建输入流，读取用户请求
            out = new PrintWriter(this.socket.getOutputStream(),true);  //根据socket构建输出流，返回响应数据
            String currenttime = null;
            String body = null;
            while (true) {
                body = in.readLine();  //读取客户端的请求
                if (body == null) {
                    break;
                }
                System.out.println("the time receive order:" + body);
                currenttime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER" ;
                out.println(currenttime);  //响应客户端的请求
            }
        } catch (IOException e) {
           if (in != null) {
               try {
                   in.close();
               } catch (IOException e1) {
                   e1.printStackTrace();
               }
           }
            if (out != null) {
                out.close();
                out = null;
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                this.socket = null;
            }
        }
    }
}
