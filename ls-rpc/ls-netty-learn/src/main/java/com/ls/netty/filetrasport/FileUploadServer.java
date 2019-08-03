package com.ls.netty.filetrasport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;



public class FileUploadServer {

    public void bind(int port) throws Exception {
        //声明两个多线程事件循环器
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //声明nio服务启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {
                	System.out.println("有客户端连接上来:"+ch.localAddress().toString());
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));// 最大长度
                    ch.pipeline().addLast(new FileUploadServerHandler());
                }
            });
            ChannelFuture f = b.bind(port).sync();//邦定端口并启动
            System.out.println("file server 等待连接：");
            f.channel().closeFuture().sync();
            System.out.println("file end");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port =  FILE_PORT;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            new FileUploadServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final int FILE_PORT = 9991;
}
