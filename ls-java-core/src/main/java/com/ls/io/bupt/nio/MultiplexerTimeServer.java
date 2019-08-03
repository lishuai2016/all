package com.ls.io.bupt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lishuai on 2017/6/17.
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;
    //初始化多路复用器，绑定监听的端口
    public MultiplexerTimeServer(int port) {
        try {

            selector = Selector.open(); // 打开selector
            serverChannel = ServerSocketChannel.open();// 打开服务端ServerSocketChannel
            serverChannel.configureBlocking(false); // 设置为非阻塞模式
            // 绑定一个本地端口，这样客户端便可以通过这个端口连接到服务器
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 注意关心的事件是OP_ACCEPT，表示只关心接受事件即接受客户端到服务器的连接
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("ServerSocket:端口" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void run() {

        while (!stop) {
            try {
                //休眠一秒，每隔一秒中selector被唤醒一次
                // select()阻塞直到注册的某个事件就绪并会更新SelectionKey的状态
                selector.select(1000);
                // 得到就绪的key集合，key中保存有就绪的事件以及对应的Channel通道
                Set<SelectionKey> SelectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = SelectionKeys.iterator();
                SelectionKey key = null;
                // 遍历选择键
                while(it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        handlerInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }

                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        //关闭多路复用器，所有注册在其上的channel和pipe资源都会被释放
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handlerInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                //接受新的连接
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //注册新的连接到selector
                sc.register(selector,SelectionKey.OP_READ);// 注意此处新增关心事件OP_READ
            }
            //
            if (key.isReadable()) {
                //读数据
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBuffers = sc.read(readBuffer);//  把channel中的数据读到buffer中
                //readBuffers
                if (readBuffers > 0) {
                    readBuffer.flip();   //读写切换，把指针移到数据的开头
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);   //把buffer中的数据读到字节数组中
                    String body = new String(bytes,"UTF-8");   //根据字节数组构建请求数据
                    System.out.println("the time receive order:" + body);
                    String currenttime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER" ;
                    dowrite(sc,currenttime);  //把响应写回给客户端
                } else if (readBuffers < 0) {
                    //链路关闭，释放资源
                    key.cancel();
                    sc.close();
                } else {
                    //0 没有读取到字节
                }
            }
        }
    }

    private void dowrite(SocketChannel channel,String response) throws IOException {
        if(response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer wrietBuffer = ByteBuffer.allocate(bytes.length);
            wrietBuffer.put(bytes);//把响应数据写到buffer中
            wrietBuffer.flip();//读写切换
            channel.write(wrietBuffer); //把buffer中的
        }
    }
}
