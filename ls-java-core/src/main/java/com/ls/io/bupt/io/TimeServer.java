package com.ls.io.bupt.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lishuai on 2017/6/17.
 */
public class TimeServer {

    public static void main(String[] ars) throws IOException {
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("ServerSocket:端口" + port);
            Socket socket = null;
            while (true) {
                socket =server.accept();
                new Thread(new TimeServerHandler(socket)).start(); //把每个链接构建个新的线程进行处理
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                System.out.println("Server close" );
                server.close();
                server = null;
            }
        }
    }
}
