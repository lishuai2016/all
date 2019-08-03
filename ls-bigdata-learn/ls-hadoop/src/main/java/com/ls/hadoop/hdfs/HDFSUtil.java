package com.ls.hadoop.hdfs;

/**
 * Created by lishuai on 2018/2/3.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class HDFSUtil {


    private HDFSUtil()  {

    }

    /**
     * 判断路径是否存在
     *
     * @param
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean exits(FileSystem fs, String path) throws IOException {
        return fs.exists(new Path(path));
    }

    /**
     * 创建文件
     *
     * @param
     * @param filePath
     * @param contents
     * @throws IOException
     */
    public static void createFile(FileSystem fs, String filePath, byte[] contents) throws IOException {
        Path path = new Path(filePath);
        FSDataOutputStream outputStream = fs.create(path);
        outputStream.write(contents);
        outputStream.close();
        //fs.close();
    }

    /**
     * 创建文件
     *
     * @param
     * @param filePath
     * @param fileContent
     * @throws IOException
     */
    public static void createFile(FileSystem fs, String filePath, String fileContent) throws IOException {
        createFile(fs, filePath, fileContent.getBytes());
    }

    /**
     * @param
     * @param localFilePath
     * @param remoteFilePath
     * @throws IOException
     */
    public static void copyFromLocalFile(FileSystem fs, String localFilePath, String remoteFilePath) throws IOException {
        Path localPath = new Path(localFilePath);
        Path remotePath = new Path(remoteFilePath);
        fs.copyFromLocalFile(true, true, localPath, remotePath);
        //fs.close();
    }

    /**
     * 删除目录或文件
     *
     * @param
     * @param remoteFilePath
     * @param recursive
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(FileSystem fs, String remoteFilePath, boolean recursive) throws IOException {
        boolean result = fs.delete(new Path(remoteFilePath), recursive);
        //fs.close();
        return result;
    }

    /**
     * 删除目录或文件(如果有子目录,则级联删除)
     *
     * @param
     * @param remoteFilePath
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(FileSystem fs, String remoteFilePath) throws IOException {
        return deleteFile(fs, remoteFilePath, true);
    }

    /**
     * 文件重命名
     *
     * @param
     * @param oldFileName
     * @param newFileName
     * @return
     * @throws IOException
     */
    public static boolean renameFile(FileSystem fs, String oldFileName, String newFileName) throws IOException {
        Path oldPath = new Path(oldFileName);
        Path newPath = new Path(newFileName);
        boolean result = fs.rename(oldPath, newPath);
        //fs.close();
        return result;
    }

    /**
     * 创建目录
     *
     * @param
     * @param dirName
     * @return
     * @throws IOException
     */
    public static boolean createDirectory(FileSystem fs, String dirName) throws IOException {

        Path dir = new Path(dirName);
        boolean result = fs.mkdirs(dir);
        //fs.close();
        return result;
    }

    /**
     * 列出指定路径下的所有文件(不包含目录)
     *
     * @param
     * @param basePath
     * @param recursive
     */
    public static RemoteIterator<LocatedFileStatus> listFiles(FileSystem fs, String basePath, boolean recursive) throws IOException {

        RemoteIterator<LocatedFileStatus> fileStatusRemoteIterator = fs.listFiles(new Path(basePath), recursive);

        return fileStatusRemoteIterator;
    }

    /**
     * 列出指定路径下的文件（非递归）
     *
     * @param
     * @param basePath
     * @return
     * @throws IOException
     */
    public static RemoteIterator<LocatedFileStatus> listFiles(FileSystem fs, String basePath) throws IOException {
        RemoteIterator<LocatedFileStatus> remoteIterator = fs.listFiles(new Path(basePath), false);
        //fs.close();
        return remoteIterator;
    }

    /**
     * 列出指定目录下的文件\子目录信息（非递归）
     *
     * @param
     * @param dirPath
     * @return
     * @throws IOException
     */
    public static FileStatus[] listStatus(FileSystem fs, String dirPath) throws IOException {
        FileStatus[] fileStatuses = fs.listStatus(new Path(dirPath));
        //fs.close();
        return fileStatuses;
    }


    /**
     * 读取文件内容
     *
     * @param conf
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(Configuration conf, FileSystem fs, String filePath) throws IOException {
        String fileContent = null;
        Path path = new Path(filePath);
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = fs.open(path);
            outputStream = new ByteArrayOutputStream(inputStream.available());
            IOUtils.copyBytes(inputStream, outputStream, conf);
            fileContent = outputStream.toString();
        } finally {
            IOUtils.closeStream(inputStream);
            IOUtils.closeStream(outputStream);
            //fs.close();
        }
        return fileContent;
    }
}
