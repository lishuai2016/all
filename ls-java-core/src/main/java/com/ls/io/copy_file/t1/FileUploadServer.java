package com.ls.io.copy_file.t1;//package copy_file.t1;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/8/31 11:35
// */
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.serialization.ClassResolvers;
//import io.netty.handler.codec.serialization.ObjectDecoder;
//import io.netty.handler.codec.serialization.ObjectEncoder;
//
//public class FileUploadServer {
//    public void bind(int port) throws Exception {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .childHandler(new ChannelInitializer<Channel>() {
//
//                @Override
//                protected void initChannel(Channel ch) throws Exception {
//                    ch.pipeline().addLast(new ObjectEncoder());
//                    ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null))); // 最大长度
//                    ch.pipeline().addLast(new FileUploadServerHandler());
//                }
//            });
//            ChannelFuture f = b.bind(port).sync();
//            f.channel().closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) {
//        int port = 8080;
//        if (args != null && args.length > 0) {
//            try {
//                port = Integer.valueOf(args[0]);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            new FileUploadServer().bind(port);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
