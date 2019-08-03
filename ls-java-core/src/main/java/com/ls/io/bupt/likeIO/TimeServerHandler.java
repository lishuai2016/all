package com.ls.io.bupt.likeIO;

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
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currenttime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println("the time receive order:" + body);
                currenttime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER" ;
                out.println(currenttime);
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
