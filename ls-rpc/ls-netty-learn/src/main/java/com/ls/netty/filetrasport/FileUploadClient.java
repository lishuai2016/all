package com.ls.netty.filetrasport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


import java.io.File;


public class FileUploadClient {
	public void connect(int port, String host,
			final FileUploadFile fileUploadFile) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//客户端只创建一个事件循环处理器
			Bootstrap b = new Bootstrap();
			//注意这里和服务端的区别，服务端：NioServerSocketChannel,客户端：NioSocketChannel
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast(new ObjectEncoder());
							ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
							ch.pipeline().addLast(new FileUploadClientHandler(fileUploadFile));
						}
					});
			ChannelFuture f = b.connect(host, port).sync();//连接服务端
			f.channel().closeFuture().sync();//关闭
			System.out.println("FileUploadClient connect()文件传输完毕");
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		int port = FILE_PORT;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			FileUploadFile uploadFile = new FileUploadFile();
			File file = new File("D:/netty/f1/1.jpg");
			String fileMd5 = file.getName();// // 文件名
			uploadFile.setFile(file);
			uploadFile.setFile_md5(fileMd5);
			uploadFile.setStarPos(0);// 文件开始位置
			new FileUploadClient().connect(port, "127.0.0.1", uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int FILE_PORT = 9991;
}
