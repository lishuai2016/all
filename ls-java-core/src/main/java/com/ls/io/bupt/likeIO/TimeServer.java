package com.ls.io.bupt.likeIO;



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
            TimeServerHanderExecutePool executePool = new TimeServerHanderExecutePool(50,1000);
            while (true) {
                socket =server.accept();
                executePool.execute(new TimeServerHandler(socket));
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
