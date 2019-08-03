package com.ls.io.io_test;

import java.io.*;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/30 14:05
 */
public class Main {


    static public void main( String args[] ) throws Exception {
        //readFileByStream();
        //writeFileByStream();
        //copyFileByStream();
        //readFileByChar();
        //writeByChar();
        copyFileByChar();
    }
    //读取文件的内容
    public static void readFileByStream() {
        String path = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/samplein.txt";

        FileInputStream fi = null;
        try {
            fi = new FileInputStream(path);   //构建输入流
            int available = fi.available();   //可读取的字节数
            byte[] bytes = new byte[available]; //按照可读的字节数构建字节数组
            fi.read(bytes);     //把输入流中的数据读入到字节数组中

            String s = new String(bytes);   //根据字节数组构建字符串，输出
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fi != null) {
                try {
                    fi.close();      //最后记得关闭输入流，释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //往文件中写内容
    public static void writeByStream() {

        String path = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/sampleout.txt";

        String content = "beijing nice!";
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path);  //构建输出流
            byte[] bytes = content.getBytes(); //把写入的内容转化为字节
            fos.write(bytes); //往输出流写数据
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();  //最后记得关闭输出流，释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //复制一个文件
    public static void copyFileByStream() {
        String srcpath = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/samplein.txt";
        String destpath = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/samplein1.txt";

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(srcpath);
            fos = new FileOutputStream(destpath);

            int available = fis.available();
            byte[] bytes = new byte[available];
            fis.read(bytes);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();      //最后记得关闭输入流，释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();  //最后记得关闭输出流，释放资源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //读取文件的内容
    public static void readFileByChar(){
        String path = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/samplein.txt";
        Reader reader = null;
        int len = 0;
        try {
            reader = new FileReader(path);
            char[] buf = new char[3];

            while((len = reader.read(buf)) != -1) { //读到文件的结尾时返回-1，否则返回读取到的字符数
                System.out.println(new String(buf ,0,len));   //输出每次读取到的字符
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
    //往文件中写内容
    public static void writeByChar(){
        String path = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/sampleout1.txt";
        Writer writer = null;

        try {
            writer = new FileWriter(path);
            writer.write("bupt");


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    //复制一个文件
    public static void copyFileByChar(){
        String srcpath = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/samplein.txt";
        String destpath = "D:/IdeaProjects/li-io-nio/src/main/java/io_test/sampleout2.txt";
        Reader reader = null;
        Writer writer = null;
        int len ;
        try {
            reader = new FileReader(srcpath);
            writer = new FileWriter(destpath);

            while((len = reader.read()) != -1) { //每次读取一个字符，读到文件的结尾时返回-1，否则返回读取到的字符数
                //System.out.println(new String(buf ,0,len));   //输出每次读取到的字符
                writer.write(len);
            }
            writer.flush();    //注意：不刷新的话，内容写不到文件中
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

}
