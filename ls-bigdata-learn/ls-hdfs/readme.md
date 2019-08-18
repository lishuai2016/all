Hadoop异常


1、
Exception in thread "main" java.lang.NoSuchFieldError: IBM_JAVA
	at org.apache.hadoop.security.UserGroupInformation.getOSLoginModuleName(UserGroupInformation.java:366)
	at org.apache.hadoop.security.UserGroupInformation.<clinit>(UserGroupInformation.java:411)
	at org.apache.hadoop.fs.FileSystem.get(FileSystem.java:158)
	at com.ls.hadoop.hdfs.HDFSMain.main(HDFSMain.java:65)



之前启动没有问题，下载突然报这个错误

Hadoop 2.7.3 


换成2.8.1版本就好了