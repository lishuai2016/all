package com.ls.netty.filetrasport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;

public class FileUploadServerHandler extends ChannelInboundHandlerAdapter {
	private int byteRead;
    private volatile int start = 0;
    private String file_dir = "D:/netty/f2";
    private Logger log= Logger.getLogger(FileUploadServerHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelActive(ctx);
    	log.info("激活channelActive()");
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	// TODO Auto-generated method stub
    	super.channelInactive(ctx);
    	log.info("无效channelInactive()");
    	ctx.flush();
    	ctx.close();
    }
    
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileUploadFile) {
            FileUploadFile ef = (FileUploadFile) msg;//自定义的一个对像，保存文件相关的属性
            byte[] bytes = ef.getBytes();
            byteRead = ef.getEndPos();
            String md5 = ef.getFile_md5();//文件名
            String path = file_dir + File.separator + md5;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + byteRead;
            System.out.println("path:"+path+","+byteRead);
            if (byteRead > 0) {
                ctx.writeAndFlush(start);
                randomAccessFile.close();
                if(byteRead!=1024 * 10){//测试每次读取10k，当文件大小不是10k时，默认文件读完， 这里可以不用调用channelInactive()
                	Thread.sleep(1000);
                	channelInactive(ctx);
                }
            } else {
            	//System.out.println("文件接收完成");
            	//ctx.flush(); 
                ctx.close();
            }
            
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        log.info("FileUploadServerHandler--exceptionCaught()");
    }
}
