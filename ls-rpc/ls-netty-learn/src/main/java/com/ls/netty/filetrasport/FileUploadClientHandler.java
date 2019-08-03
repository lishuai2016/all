package com.ls.netty.filetrasport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUploadClientHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
	private volatile int start = 0;
	private volatile int lastLength = 0;
	public RandomAccessFile randomAccessFile;
	private FileUploadFile fileUploadFile;
	private Logger log = Logger.getLogger(FileUploadClientHandler.class);

	public FileUploadClientHandler(FileUploadFile ef) {
		if (ef.getFile().exists()) {
			if (!ef.getFile().isFile()) {
				System.out.println("Not a file :" + ef.getFile());
				return;
			}
		}
		this.fileUploadFile = ef;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		log.info("�ͻ��˽��������ļ�channelInactive()");
	}

	public void channelActive(ChannelHandlerContext ctx) {
		//System.out.println("�ͻ��ˣ�channelActive()");
		try {
			randomAccessFile = new RandomAccessFile(fileUploadFile.getFile(), "r");
			randomAccessFile.seek(fileUploadFile.getStarPos());
			// lastLength = (int) randomAccessFile.length() / 10;
			lastLength = 1024 * 10;
			byte[] bytes = new byte[lastLength];
			if ((byteRead = randomAccessFile.read(bytes)) != -1) {
				fileUploadFile.setEndPos(byteRead);
				fileUploadFile.setBytes(bytes);
				ctx.writeAndFlush(fileUploadFile);
			} else {
			}
			log.info("channelActive()�ļ��Ѿ����� "+byteRead);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof Integer) {
			start = (Integer) msg;
			if (start != -1) {
				randomAccessFile = new RandomAccessFile(
						fileUploadFile.getFile(), "r");
				randomAccessFile.seek(start);
				log.info("���ȣ�" + (randomAccessFile.length() - start));
				int a = (int) (randomAccessFile.length() - start);
				int b = (int) (randomAccessFile.length() / 1024*2);
				if (a < lastLength) {
					lastLength = a;
				}
				log.info("�ļ����ȣ�" + (randomAccessFile.length())+",start:"+start+",a:"+a+",b:"+b+",lastLength:"+lastLength);
				byte[] bytes = new byte[lastLength];
				// log.info("-----------------------------" + bytes.length);
				if ((byteRead = randomAccessFile.read(bytes)) != -1
						&& (randomAccessFile.length() - start) > 0) {
					// log.info("byte ���ȣ�" + bytes.length);
					fileUploadFile.setEndPos(byteRead);
					fileUploadFile.setBytes(bytes);
					try {
						ctx.writeAndFlush(fileUploadFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					randomAccessFile.close();
					ctx.close();
					log.info("�ļ��Ѿ�����channelRead()--------" + byteRead);
				}
			}
		}
	}

	// @Override
	// public void channelRead(ChannelHandlerContext ctx, Object msg) throws
	// Exception {
	// System.out.println("Server is speek ��"+msg.toString());
	// FileRegion filer = (FileRegion) msg;
	// String path = "E://Apk//APKMD5.txt";
	// File fl = new File(path);
	// fl.createNewFile();
	// RandomAccessFile rdafile = new RandomAccessFile(path, "rw");
	// FileRegion f = new DefaultFileRegion(rdafile.getChannel(), 0,
	// rdafile.length());
	//
	// System.out.println("This is" + ++counter + "times receive server:["
	// + msg + "]");
	// }

	// @Override
	// public void channelReadComplete(ChannelHandlerContext ctx) throws
	// Exception {
	// ctx.flush();
	// }

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	// @Override
	// protected void channelRead0(ChannelHandlerContext ctx, String msg)
	// throws Exception {
	// String a = msg;
	// System.out.println("This is"+
	// ++counter+"times receive server:["+msg+"]");
	// }

}
