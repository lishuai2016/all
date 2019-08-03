package com.ls.netty.socketio;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2019-02-17 23:29
 */
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value=1)
public class MyCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer server;


    @Autowired
    public MyCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }


    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("socket.io启动成功！");
    }
}
