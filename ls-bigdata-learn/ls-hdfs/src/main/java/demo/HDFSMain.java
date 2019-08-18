package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;

/**
 -rw-r--r--	root	406695	1536410790934	hdfs://node1:9000/1.pdf
 cacheHosts:
 hosts: node3，node2，
 block size: 406695
 block start offset: 0
 ----------------------------------------------
 -rw-r--r--	root	214092195	1536420576296	hdfs://node1:9000/hadoop-2.7.3.tar.gz
 cacheHosts:
 hosts: node3，node2，
 block size: 134217728
 block start offset: 0
 ----------------------------------------------
 cacheHosts:
 hosts: node3，node2，
 block size: 79874467
 block start offset: 134217728
 ----------------------------------------------
 -rw-r--r--	root	18	1536407062764	hdfs://node1:9000/ls.txt
 cacheHosts:
 hosts: node3，node2，
 block size: 18
 block start offset: 0
 ----------------------------------------------
 -rw-r--r--	root	18	1536407306740	hdfs://node1:9000/ls1.txt
 cacheHosts:
 hosts: node3，node2，
 block size: 18
 block start offset: 0
 ----------------------------------------------
 -rw-r--r--	root	2022	1536406858464	hdfs://node1:9000/test.txt
 cacheHosts:
 hosts: node3，node2，
 block size: 2022
 block start offset: 0
 ----------------------------------------------
 -rw-r--r--	root	406695	1536410790934	hdfs://node1:9000/1.pdf
 -rw-r--r--	root	214092195	1536420576296	hdfs://node1:9000/hadoop-2.7.3.tar.gz
 drwxr-xr-x	root	0	1536484416625	hdfs://node1:9000/lishuai
 drwxr-xr-x	root	0	1536484416625	hdfs://node1:9000/lishuai/1
 drwxr-xr-x	root	0	1536484416626	hdfs://node1:9000/lishuai/1/2
 drwxr-xr-x	root	0	1536484416626	hdfs://node1:9000/lishuai/1/2/3
 -rw-r--r--	root	18	1536407062764	hdfs://node1:9000/ls.txt
 -rw-r--r--	root	18	1536407306740	hdfs://node1:9000/ls1.txt
 drwxr-xr-x	root	0	1536486187085	hdfs://node1:9000/test
 -rw-r--r--	root	2022	1536406858464	hdfs://node1:9000/test.txt
 */

public class HDFSMain {

	public static void main(String[] args) throws Exception {
//		System.setProperty("hadoop.home.dir", "D:\\hadoop\\hadoop-2.7.3");
//		Configuration conf  = new Configuration();
//		conf.set("fs.defaultFS", "hdfs://node1:9000");
//		conf.set("dfs.replication","3");
//		FileSystem hdfs = FileSystem.get(conf);

		System.setProperty("hadoop.home.dir", "D:\\hadoop\\hadoop-2.7.3");
		Configuration conf  = new Configuration();
		conf.set("dfs.replication","3");
		FileSystem hdfs = FileSystem.get(new URI("hdfs://node1:9000"), conf, "root");





		//System.out.println(HDFSUtil.exits(hdfs,"/test"));
		//System.out.println(HDFSUtil.createDirectory(hdfs,"/test"));


//		Path path = new Path("/lishuai/1/2/3");
//		boolean tags = hdfs.mkdirs(path);
//		System.out.println(tags);






		//读取本地文件
//		InputStream in = new FileInputStream("D:/1.pdf");
//		//在Hdfs上创建一个文件，返回输出流
//		OutputStream out = hdfs.create(new Path("/1.pdf"));
//		//输入 ---》  输出
//		IOUtils.copyBytes(in, out, 4096, true);


//		Path path = new Path("/lishuai");
//		boolean tags = hdfs.mkdirs(path);
//		System.out.println(tags);

		//文件上传
		//upfile(hdfs);
		//downfile(hdfs);
		//renamefile(hdfs);
		//deletefile(hdfs);
		listfiles(hdfs);//只有文件，不包含目录
		fileAll(hdfs);//既有文件有包含目录
		//readfilebyline(hdfs);
		//printToConsole(hdfs);
		//getFileByStream(hdfs);
		//getPartFileByStream(hdfs);
		//getFirstBlockFileByStream(hdfs);
	}

	//从一个输入流获取数据，然后上传到HDFS上
	public static void upfileByStream(FileSystem fileSystem, InputStream in, String outPath)throws Exception, IOException {
				//在Hdfs上创建一个文件，返回输出流
		OutputStream out = fileSystem.create(new Path(outPath));
		//输入 ---》  输出
		IOUtils.copyBytes(in, out, 4096, true);
		in.close();
		out.close();
	}

	public static void getFirstBlockFileByStream(FileSystem fileSystem) throws Exception, IOException {
		FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://node1:9000/input/word.txt"));
		FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/3.txt"));
		long offset = 0;
		byte[] b = new byte[4096];
		while (fsDataInputStream.read(offset, b, 0, 4096) != -1) {
			if (offset >= 12) {
				return;
			}
			fileOutputStream.write(b);
			offset += 4096;
		}
		fileOutputStream.flush();
		fsDataInputStream.close();
		fileOutputStream.close();
	}


	//使用文件流的方式随机读取一部分的数据
	public static void getPartFileByStream(FileSystem fileSystem) throws Exception, IOException {
		FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://node1:9000/input/word.txt"));
		fsDataInputStream.seek(10);//偏移10个字符读取
		FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/2.txt"));
		IOUtils.copyBytes(fsDataInputStream,fileOutputStream,4096,true);
	}

	//使用文件流的方式获取HDFS上的文件数据
	public static void getFileByStream(FileSystem fileSystem) throws Exception, IOException {
		FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://node1:9000/input/word.txt"));
		FileOutputStream fileOutputStream = new FileOutputStream(new File("F:/1.txt"));
		IOUtils.copyBytes(fsDataInputStream,fileOutputStream,4096);
	}


	//将文件打印到控制台
	public static void printToConsole(FileSystem fileSystem) throws Exception, IOException {
		FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://node1:9000/input/word.txt"));
		IOUtils.copyBytes(fsDataInputStream, System.out, 1024);
	}
	//逐行读取文本数据
	public static void readfilebyline(FileSystem fileSystem) throws Exception, IOException {
		FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://node1:9000/input/word.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(fsDataInputStream));
		String line = null;
		while ((line = reader.readLine()) != null) { // 读取文件行内容
			System.out.println("Record: " + line);
		}
	}











	//文件上传
	public static void upfile(FileSystem fileSystem) throws Exception, IOException {
		boolean isDone = fileSystem.mkdirs(new Path("/lishuai/hadoop"));
		Path srcPath = new Path("F:\\hadoop\\hadoop-2.7.3.tar.gz");
		Path dstPath = new Path("/lishuai/hadoop/hadoop-2.7.3.tar.gz");
		fileSystem.copyFromLocalFile(srcPath, dstPath);
	}


	/**
	 *
	 四个参数的含义分别是：是否删除HDFS集群下的原始文件、HDFS文件地址、本地文件地址、是否使用原生进行操作。
	 四个参数中，起作用的就是是否使用本地文件系统进行操作这个参数，如果配置了winutils.exe，
	 下传文件就会使用winutils.exe，如果没有配置建议使用该重载方法。
	 */
	//文件下载
	public static void downfile(FileSystem fileSystem) throws Exception, IOException {
		Path hadoopPath = new Path("/input/word.txt");
		Path dstPath = new Path("F://");
		fileSystem.copyToLocalFile(false, hadoopPath, dstPath, true);
	}

	//移动文件  rename方法可以用来移动文件，也可以用来重命名文件。
	public static void renamefile(FileSystem fileSystem) throws Exception, IOException {
		Path srcPath = new Path("/lishuai/hadoop/hadoop-2.7.3.tar.gz");
		Path disPath = new Path("/input/hadoop-2.6.1.tar.gz");
		boolean flag = fileSystem.rename(srcPath,disPath);
		System.out.println(flag);
	}

	//删除文件  delete方法可以用来递归删除某个目录下的文件信息
	public static void deletefile(FileSystem fileSystem) throws Exception, IOException {
		boolean flag = fileSystem.delete(new Path("/input/hadoop-2.6.1.tar.gz"), true);
		System.out.println(flag);
	}

	/**
	 为什么使用RemoteIterator<LocatedFileStatus>，这是一个远程的迭代器，由于HDFS服务器数据量比较多，
	 不能一次性返回所有文件信息(可能撑爆内存)，所以一边迭代一边从远程服务器上获取。分治的思想。
	 */
	//打印文件列表
	public static void listfiles(FileSystem fileSystem) throws Exception, IOException {
		RemoteIterator<LocatedFileStatus> fileLists = fileSystem.listFiles(new Path("/"), true);
		while (fileLists.hasNext()) {
			LocatedFileStatus locatedFileStatus = fileLists.next();
			// 按照以下格式打印HDFS上的文件信息
			// drwxr-xr-x   - root supergroup          0 2015-12-18 00:24 /itcast/hadoop

			String fileType = "-";
			System.out.print(fileType);//但因文件的类型

			String authority = locatedFileStatus.getPermission().toString();
			System.out.print(authority + "\t");//打印文件的权限

			String user = locatedFileStatus.getOwner();
			System.out.print(user + "\t");//打印文件所属的的用户

			long size = locatedFileStatus.getLen();
			System.out.print(size + "\t");//打印文件的大小

			long date = locatedFileStatus.getModificationTime();
			System.out.print(date + "\t");//打印文件的时间戳

			String path = locatedFileStatus.getPath().toString();
			System.out.print(path + "\t");//打印文件的路径

			System.out.println();

			for (BlockLocation blockLocation : locatedFileStatus.getBlockLocations()) {
				System.out.print("cacheHosts: ");
				for (String hosts : blockLocation.getCachedHosts()) {
					System.out.print(hosts + "，");//打印block的cachehosts
				}
				System.out.println();

				System.out.print("hosts: ");
				for (String hosts : blockLocation.getHosts()) {
					System.out.print(hosts + "，");//打印文件block所在的服务器
				}
				System.out.println();

				System.out.print("block size: ");
				System.out.println(blockLocation.getLength());//打印block的大小

				System.out.print("block start offset: ");
				System.out.println(blockLocation.getOffset()); //文件开始偏移量

				System.out.println("----------------------------------------------");

			}

		}
	}
	//查看HDFS的所有文件信息(使用递归算法)
	public static void fileAll(FileSystem fileSystem) throws Exception {
		printContent(fileSystem,new Path("/"));
	}

	public  static void printContent(FileSystem fileSystem, Path path) throws Exception {
		FileStatus[] fileStatuses = fileSystem.listStatus(path);
		for (FileStatus fileStatus : fileStatuses) {
			String fileType = "d";
			if (fileStatus.isFile()) {
				fileType = "-";
			} else if (fileStatus.isSymlink()) {
				fileType = "l";
			}
			System.out.println(fileType + fileStatus.getPermission() + "\t" + fileStatus.getOwner() + "\t" + fileStatus.getLen() + "\t" + fileStatus.getModificationTime() + "\t" + fileStatus.getPath());
			if (fileStatus.isDirectory()) {
				printContent(fileSystem, fileStatus.getPath());
			}
		}
	}


}
