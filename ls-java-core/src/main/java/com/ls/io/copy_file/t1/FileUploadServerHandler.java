package com.ls.io.copy_file.t1;//package copy_file.t1;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/8/31 11:35
// */
//import io.netty.channel.ChannelHandlerAdapter;
//import io.netty.channel.ChannelHandlerContext;
//
//
//import java.io.File;
//import java.io.RandomAccessFile;
//
//public class FileUploadServerHandler extends ChannelHandlerAdapter {
//    private int byteRead;
//    private volatile int start = 0;
//    private String file_dir = "D:";
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof FileUploadFile) {
//            FileUploadFile ef = (FileUploadFile) msg;
//            byte[] bytes = ef.getBytes();
//            byteRead = ef.getEndPos();
//            String md5 = ef.getFile_md5();//文件名
//            String path = file_dir + File.separator + md5;
//            File file = new File(path);
//            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
//            randomAccessFile.seek(start);
//            randomAccessFile.write(bytes);
//            start = start + byteRead;
//            if (byteRead > 0) {
//                ctx.writeAndFlush(start);
//            } else {
//                randomAccessFile.close();
//                ctx.close();
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
