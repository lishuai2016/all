
<!-- TOC -->

- [第210讲-Spark SQL实战开发进阶-Hive 0.13安装与测试](#第210讲-spark-sql实战开发进阶-hive-013安装与测试)
    - [1、安装hive包](#1安装hive包)
    - [2、安装mysql](#2安装mysql)
    - [3、配置hive-site.xml](#3配置hive-sitexml)
    - [4、配置hive-env.sh和hive-config.sh](#4配置hive-envsh和hive-configsh)
    - [5、验证hive是否安装成功](#5验证hive是否安装成功)
- [第211讲-Spark SQL实战开发进阶-Thrift JDBC、ODBC Server](#第211讲-spark-sql实战开发进阶-thrift-jdbcodbc-server)
- [第212讲-Spark SQL实战开发进阶-CLI命令行使用](#第212讲-spark-sql实战开发进阶-cli命令行使用)
- [第213讲-Spark SQL实战开发进阶-综合案例2：新闻网站关键指标离线统计](#第213讲-spark-sql实战开发进阶-综合案例2新闻网站关键指标离线统计)
- [第214讲-Spark SQL实战开发进阶-综合案例2：页面pv统计以及排序和企业级项目开发流程说明](#第214讲-spark-sql实战开发进阶-综合案例2页面pv统计以及排序和企业级项目开发流程说明)
- [第215讲-Spark SQL实战开发进阶-综合案例2：页面uv统计以及排序和count(distinct) bug说明](#第215讲-spark-sql实战开发进阶-综合案例2页面uv统计以及排序和countdistinct-bug说明)
- [第216讲-Spark SQL实战开发进阶-综合案例2：新用户注册比例统计](#第216讲-spark-sql实战开发进阶-综合案例2新用户注册比例统计)
- [第217讲-Spark SQL实战开发进阶-综合案例2：用户跳出率统计](#第217讲-spark-sql实战开发进阶-综合案例2用户跳出率统计)
- [第218讲-Spark SQL实战开发进阶-综合案例2：版块热度排行榜统计](#第218讲-spark-sql实战开发进阶-综合案例2版块热度排行榜统计)
- [第219讲-Spark SQL实战开发进阶-综合案例2：测试与调试](#第219讲-spark-sql实战开发进阶-综合案例2测试与调试)

<!-- /TOC -->

# 第210讲-Spark SQL实战开发进阶-Hive 0.13安装与测试

## 1、安装hive包
1、将课程提供的hive-0.13.1-cdh5.3.6.tar.gz使用WinSCP上传到sparkproject1的/usr/local目录下。
2、解压缩hive安装包：tar -zxvf hive-0.13.1-cdh5.3.6.tar.gz
3、重命名hive目录：mv hive-0.13.1-cdh5.3.6 hive
4、配置hive相关的环境变量
vi ~/.bashrc
export HIVE_HOME=/usr/local/hive
export PATH=$HIVE_HOME/bin
source ~/.bashrc


## 2、安装mysql
1、在sparkproject1上安装mysql。
2、使用yum安装mysql server。
yum install -y mysql-server
service mysqld start
chkconfig mysqld on
3、使用yum安装mysql connector
yum install -y mysql-connector-java
4、将mysql connector拷贝到hive的lib包中
cp /usr/share/java/mysql-connector-java-5.1.17.jar /usr/local/hive/lib
5、在mysql上创建hive元数据库，创建hive账号，并进行授权
create database if not exists hive_metadata;
grant all privileges on hive_metadata.* to 'hive'@'%' identified by 'hive';
grant all privileges on hive_metadata.* to 'hive'@'localhost' identified by 'hive';
grant all privileges on hive_metadata.* to 'hive'@'sparkupgrade1' identified by 'hive';
flush privileges;
use hive_metadata;


## 3、配置hive-site.xml
mv hive-default.xml.template hive-site.xml

<property>
  <name>javax.jdo.option.ConnectionURL</name>
  <value>jdbc:mysql://spark1:3306/hive_metadata?createDatabaseIfNotExist=true</value>
</property>
<property>
  <name>javax.jdo.option.ConnectionDriverName</name>
  <value>com.mysql.jdbc.Driver</value>
</property>
<property>
  <name>javax.jdo.option.ConnectionUserName</name>
  <value>hive</value>
</property>
<property>
  <name>javax.jdo.option.ConnectionPassword</name>
  <value>hive</value>
</property>


## 4、配置hive-env.sh和hive-config.sh

mv hive-env.sh.template hive-env.sh

vi /usr/local/hive/bin/hive-config.sh
export JAVA_HOME=/usr/java/latest
export HIVE_HOME=/usr/local/hive
export HADOOP_HOME=/usr/local/hadoop

## 5、验证hive是否安装成功

直接输入hive命令，可以进入hive命令行

create table users(id int, name string) 
load data local inpath '/usr/local/users.txt' into table users
select name from users 



# 第211讲-Spark SQL实战开发进阶-Thrift JDBC、ODBC Server

Spark SQL的Thrift JDBC/ODBC server是基于Hive 0.13的HiveServer2实现的。这个服务启动之后，最主要的功能就是可以让我们通过
Java JDBC来以编程的方式调用Spark SQL。此外，在启动该服务之后，可以通过Spark或Hive 0.13自带的beeline工具来进行测试。

要启动JDBC/ODBC server，主要执行Spark的sbin目录下的start-thriftserver.sh命令即可

start-thriftserver.sh命令可以接收所有spark-submit命令可以接收的参数，额外增加的一个参数是--hiveconf，可以用于指定一些
Hive的配置属性。可以通过执行./sbin/start-thriftserver.sh --help来查看所有可用参数的列表。默认情况下，启动的服务会在
localhost:10000地址上监听请求。

可以使用两种方式来改变服务监听的地址

第一种：指定环境变量
export HIVE_SERVER2_THRIFT_PORT=<listening-port>
export HIVE_SERVER2_THRIFT_BIND_HOST=<listening-host>
./sbin/start-thriftserver.sh \
  --master <master-uri> \
  ...
  
第二种：使用命令的参数
./sbin/start-thriftserver.sh \
  --hiveconf hive.server2.thrift.port=<listening-port> \
  --hiveconf hive.server2.thrift.bind.host=<listening-host> \
  --master <master-uri>
  ...
  
hdfs dfs -chmod 777 /tmp/hive-root

./sbin/start-thriftserver.sh \
--jars /usr/local/hive/lib/mysql-connector-java-5.1.17.jar
  
这两种方式的区别就在于，第一种是针对整个机器上每次启动服务都生效的; 第二种仅仅针对本次启动生效

接着就可以通过Spark或Hive的beeline工具来测试Thrift JDBC/ODBC server
在Spark的bin目录中，执行beeline命令（当然，我们也可以使用Hive自带的beeline工具）：./bin/beeline
进入beeline命令行之后，连接到JDBC/ODBC server上去：beeline> !connect jdbc:hive2://localhost:10000

beeline通常会要求你输入一个用户名和密码。在非安全模式下，我们只要输入本机的用户名（比如root），以及一个空的密码即可。
对于安全模式，需要根据beeline的文档来进行认证。

除此之外，大家要注意的是，如果我们想要直接通过JDBC/ODBC服务访问Spark SQL，并直接对Hive执行SQL语句，那么就需要将Hive
的hive-site.xml配置文件放在Spark的conf目录下。

Thrift JDBC/ODBC server也支持通过HTTP传输协议发送thrift RPC消息。使用以下方式的配置可以启动HTTP模式：

命令参数
./sbin/start-thriftserver.sh \
  --hive.server2.transport.mode=http \
  --hive.server2.thrift.http.port=10001 \
  --hive.server2.http.endpoint=cliservice \
  --master <master-uri>
  ...
  
./sbin/start-thriftserver.sh \
  --jars /usr/local/hive/lib/mysql-connector-java-5.1.17.jar \
  --hiveconf hive.server2.transport.mode=http \
  --hiveconf hive.server2.thrift.http.port=10001 \
  --hiveconf hive.server2.http.endpoint=cliservice 
  
beeline连接服务时指定参数
beeline> !connect jdbc:hive2://localhost:10001/default?hive.server2.transport.mode=http;hive.server2.thrift.http.path=cliservice

最重要的，当然是通过Java JDBC的方式，来访问Thrift JDBC/ODBC server，调用Spark SQL，并直接查询Hive中的数据

<dependency>
  <groupId>org.apache.hive</groupId>
  <artifactId>hive-jdbc</artifactId>
  <version>0.13.0</version>
</dependency>
<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpclient</artifactId>
  <version>4.4.1</version>
</dependency>
<dependency>
  <groupId>org.apache.httpcomponents</groupId>
  <artifactId>httpcore</artifactId>
  <version>4.4.1</version>
</dependency>

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ThriftJDBCServerTest {
	
	public static void main(String[] args) {
		String sql = "select name from users where id=?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");  
			
			conn = DriverManager.getConnection("jdbc:hive2://192.168.0.103:10001/default?hive.server2.transport.mode=http;hive.server2.thrift.http.path=cliservice", 
					"root", 
					"");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);  
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String name = rs.getString(1);
				System.out.println(name);  
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
}



# 第212讲-Spark SQL实战开发进阶-CLI命令行使用

Spark SQL CLI是一个很方便的工具，可以用来在本地模式下运行Hive的元数据服务，并且通过命令行执行针对Hive的SQL查询。但是
我们要注意的是，Spark SQL CLI是不能与Thrift JDBC server进行通信的。

如果要启动Spark SQL CLI，只要执行Spark的bin目录下的spark-sql命令即可
./bin/spark-sql --jars /usr/local/hive/lib/mysql-connector-java-5.1.17.jar

这里同样要注意的是，必须将我们的hive-site.xml文件放在Spark的conf目录下。

我们也可以通过执行./bin/spark-sql --help命令，来获取该命令的所有帮助选项。



# 第213讲-Spark SQL实战开发进阶-综合案例2：新闻网站关键指标离线统计

案例背景

新闻网站
1、版块
2、新闻页面
3、新用户注册
4、用户跳出

案例需求分析

每天每个页面的PV：PV是Page View，是指一个页面被所有用户访问次数的总和，页面被访问一次就被记录1次PV
每天每个页面的UV：UV是User View，是指一个页面被多少个用户访问了，一个用户访问一次是1次UV，一个用户访问多次还是1次UV
新用户注册比率：当天注册用户数 / 当天未注册用户的访问数
用户跳出率：IP只浏览了一个页面就离开网站的次数/网站总访问数（PV）
版块热度排行榜：根据每个版块每天被访问的次数，做出一个排行榜

网站日志格式
date timestamp userid pageid section action 

日志字段说明
date: 日期，yyyy-MM-dd格式
timestamp: 时间戳
userid: 用户id
pageid: 页面id
section: 新闻版块
action: 用户行为，两类，点击页面和注册

模拟数据生成程序
模式数据演示

在hive中创建访问日志表

create table news_access (
  date string,
  timestamp bigint,
  userid bigint,
  pageid bigint,
  section string,
  action string) 

将模拟数据导入hive表中

load data local inpath '/usr/local/test/news_access.log' into table news_access;


```java
package cn.spark.study.sql.upgrade.news;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 离线数据生成器
 * @author Administrator
 *
 */
public class OfflineDataGenerator {
	
	public static void main(String[] args) throws Exception {
		StringBuffer buffer = new StringBuffer("");  
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Random random = new Random();
		String[] sections = new String[] {"country", "international", "sport", "entertainment", "movie", "carton", "tv-show", "technology", "internet", "car"};
		
		int[] newOldUserArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		
		// 生成日期，默认就是昨天
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());  
		cal.add(Calendar.DAY_OF_YEAR, -1);  
		Date yesterday = cal.getTime();
		
		String date = sdf.format(yesterday);
		
		// 生成1000条访问数据
		for(int i = 0; i < 1000; i++) {
			// 生成时间戳
			long timestamp = new Date().getTime();
			
			// 生成随机userid（默认1000注册用户，每天1/10的访客是未注册用户）
			Long userid = 0L;
			
			int newOldUser = newOldUserArr[random.nextInt(10)];
			if(newOldUser == 1) {
				userid = null;
			} else {
				userid = (long) random.nextInt(1000);
			}
			
			// 生成随机pageid（总共1k个页面）
			Long pageid = (long) random.nextInt(1000);  
			
			// 生成随机版块（总共10个版块）
			String section = sections[random.nextInt(10)]; 
			
			// 生成固定的行为，view
			String action = "view"; 
			 
			buffer.append(date).append("")  
					.append(timestamp).append("")
					.append(userid).append("")
					.append(pageid).append("")
					.append(section).append("")
					.append(action).append("\n");
		}
		
		// 生成10条注册数据
		for(int i = 0; i < 10; i++) {
			// 生成时间戳
			long timestamp = new Date().getTime();
			
			// 新用户都是userid为null
			Long userid = null;

			// 生成随机pageid，都是null
			Long pageid = null;  
			
			// 生成随机版块，都是null
			String section = null; 
			
			// 生成固定的行为，view
			String action = "register"; 
			
			buffer.append(date).append("")  
					.append(timestamp).append("")
					.append(userid).append("")
					.append(pageid).append("")
					.append(section).append("")
					.append(action).append("\n");
		}
		
		PrintWriter pw = null;  
		try {
			pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\Users\\Administrator\\Desktop\\access.log")));
			pw.write(buffer.toString());  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.close();
		}
	}
	
}

```


# 第214讲-Spark SQL实战开发进阶-综合案例2：页面pv统计以及排序和企业级项目开发流程说明




# 第215讲-Spark SQL实战开发进阶-综合案例2：页面uv统计以及排序和count(distinct) bug说明




# 第216讲-Spark SQL实战开发进阶-综合案例2：新用户注册比例统计




# 第217讲-Spark SQL实战开发进阶-综合案例2：用户跳出率统计




# 第218讲-Spark SQL实战开发进阶-综合案例2：版块热度排行榜统计



# 第219讲-Spark SQL实战开发进阶-综合案例2：测试与调试


```java
package cn.spark.study.sql.upgrade.news;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.hive.HiveContext;

/**
 * 新闻网站关键指标离线统计Spark作业
 * @author Administrator
 *
 */
public class NewsOfflineStatSpark {

	public static void main(String[] args) {
		// 一般来说，在小公司中，可能就是将我们的spark作业使用linux的crontab进行调度
			// 将作业jar放在一台安装了spark客户端的机器上，并编写了对应的spark-submit shell脚本
			// 在crontab中可以配置，比如说每天凌晨3点执行一次spark-submit shell脚本，提交一次spark作业
			// 一般来说，离线的spark作业，每次运行，都是去计算昨天的数据
		// 大公司总，可能是使用较为复杂的开源大数据作业调度平台，比如常用的有azkaban、oozie等
			// 但是，最大的那几个互联网公司，比如说BAT、美团、京东，作业调度平台，都是自己开发的
			// 我们就会将开发好的Spark作业，以及对应的spark-submit shell脚本，配置在调度平台上，几点运行
			// 同理，每次运行，都是计算昨天的数据
		
		// 一般来说，每次spark作业计算出来的结果，实际上，大部分情况下，都会写入mysql等存储
		// 这样的话，我们可以基于mysql，用java web技术开发一套系统平台，来使用图表的方式展示每次spark计算
		// 出来的关键指标
		// 比如用折线图，可以反映最近一周的每天的用户跳出率的变化
		
		// 也可以通过页面，给用户提供一个查询表单，可以查询指定的页面的最近一周的pv变化
		// date pageid pv
		// 插入mysql中，后面用户就可以查询指定日期段内的某个page对应的所有pv，然后用折线图来反映变化曲线
		
		// 拿到昨天的日期，去hive表中，针对昨天的数据执行SQL语句
		String yesterday = getYesterday();
		
		// 创建SparkConf以及Spark上下文
		SparkConf conf = new SparkConf()
				.setAppName("NewsOfflineStatSpark")    
				.setMaster("local");  
		JavaSparkContext sc = new JavaSparkContext(conf);
		HiveContext hiveContext = new HiveContext(sc.sc());
	
		// 开发第一个关键指标：页面pv统计以及排序
		calculateDailyPagePv(hiveContext, yesterday);  
		// 开发第二个关键指标：页面uv统计以及排序
		calculateDailyPageUv(hiveContext, yesterday);
		// 开发第三个关键指标：新用户注册比率统计
		calculateDailyNewUserRegisterRate(hiveContext, yesterday);
		// 开发第四个关键指标：用户跳出率统计
		calculateDailyUserJumpRate(hiveContext, yesterday);
		// 开发第五个关键指标：版块热度排行榜
		calculateDailySectionPvSort(hiveContext, yesterday);
		
		// 关闭Spark上下文
		sc.close();
	}

	/**
	 * 获取昨天的字符串类型的日期
	 * @return 日期
	 */
	private static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);  
		
		Date yesterday = cal.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(yesterday);
	}
	
	/**
	 * 计算每天每个页面的pv以及排序
	 *   排序的好处：排序后，插入mysql，java web系统要查询每天pv top10的页面，直接查询mysql表limit 10就可以
	 *   如果我们这里不排序，那么java web系统就要做排序，反而会影响java web系统的性能，以及用户响应时间
	 */
	private static void calculateDailyPagePv(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "pageid,"
					+ "pv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "pageid,"
						+ "count(*) pv "
					+ "FROM news_access "
					+ "WHERE action='view' "
					+ "AND date='" + date + "' " 
					+ "GROUP BY date,pageid "
				+ ") t "
				+ "ORDER BY pv DESC ";  
		
		DataFrame df = hiveContext.sql(sql);
	
		// 在这里，我们也可以转换成一个RDD，然后对RDD执行一个foreach算子
		// 在foreach算子中，将数据写入mysql中
		
		df.show();  
	}
	
	/**
	 * 计算每天每个页面的uv以及排序
	 *   Spark SQL的count(distinct)语句，有bug，默认会产生严重的数据倾斜
	 *   只会用一个task，来做去重和汇总计数，性能很差
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyPageUv(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "pageid,"
					+ "uv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "pageid,"
						+ "count(*) uv "
					+ "FROM ( "
						+ "SELECT "
							+ "date,"
							+ "pageid,"
							+ "userid "
						+ "FROM news_access "
						+ "WHERE action='view' "
						+ "AND date='" + date + "' "
						+ "GROUP BY date,pageid,userid "
					+ ") t2 "
					+ "GROUP BY date,pageid "
				+ ") t "
				+ "ORDER BY uv DESC ";
		
		DataFrame df = hiveContext.sql(sql);
		
		df.show();
	}
	
	/**
	 * 计算每天的新用户注册比例
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyNewUserRegisterRate(
			HiveContext hiveContext, String date) {
		// 昨天所有访问行为中，userid为null，新用户的访问总数
		String sql1 = "SELECT count(*) FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NULL";
		// 昨天的总注册用户数
		String sql2 = "SELECT count(*) FROM news_access WHERE action='register' AND date='" + date + "' ";
	
		// 执行两条SQL，获取结果
		Object result1 = hiveContext.sql(sql1).collect()[0].get(0);
		long number1 = 0L;
		if(result1 != null) {
			number1 = Long.valueOf(String.valueOf(result1));  
		}
		
		Object result2 = hiveContext.sql(sql2).collect()[0].get(0);
		long number2 = 0L;
		if(result2 != null) {
			number2 = Long.valueOf(String.valueOf(result2));  
		}
		
		// 计算结果
		System.out.println("======================" + number1 + "======================");  
		System.out.println("======================" + number2 + "======================");  
		double rate = (double)number2 / (double)number1;
		System.out.println("======================" + formatDouble(rate, 2) + "======================");  
	}
	
	/**
	 * 计算每天的用户跳出率
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailyUserJumpRate(
			HiveContext hiveContext, String date) {
		// 计算已注册用户的昨天的总的访问pv
		String sql1 = "SELECT count(*) FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NOT NULL ";
		
		// 已注册用户的昨天跳出的总数
		String sql2 = "SELECT count(*) FROM ( SELECT count(*) cnt FROM news_access WHERE action='view' AND date='" + date + "' AND userid IS NOT NULL GROUP BY userid HAVING cnt=1 ) t ";
		
		// 执行两条SQL，获取结果
		Object result1 = hiveContext.sql(sql1).collect()[0].get(0);
		long number1 = 0L;
		if(result1 != null) {
			number1 = Long.valueOf(String.valueOf(result1));  
		}
		
		Object result2 = hiveContext.sql(sql2).collect()[0].get(0);
		long number2 = 0L;
		if(result2 != null) {
			number2 = Long.valueOf(String.valueOf(result2));  
		}
		
		// 计算结果
		System.out.println("======================" + number1 + "======================");  
		System.out.println("======================" + number2 + "======================");  
		double rate = (double)number2 / (double)number1;
		System.out.println("======================" + formatDouble(rate, 2) + "======================");
	}
	
	/**
	 * 计算每天的版块热度排行榜
	 * @param hiveContext
	 * @param date
	 */
	private static void calculateDailySectionPvSort(
			HiveContext hiveContext, String date) {
		String sql = 
				"SELECT "
					+ "date,"
					+ "section,"
					+ "pv "
				+ "FROM ( "
					+ "SELECT "
						+ "date,"
						+ "section,"
						+ "count(*) pv "
					+ "FROM news_access "
					+ "WHERE action='view' "
					+ "AND date='" + date + "' "
					+ "GROUP BY date,section "
				+ ") t "
				+ "ORDER BY pv DESC ";
		
		DataFrame df = hiveContext.sql(sql);
		  
		df.show();
	}
	
	/**
	 * 格式化小数
	 * @param str 字符串
	 * @param scale 四舍五入的位数
	 * @return 格式化小数
	 */
	private static double formatDouble(double num, int scale) {
		BigDecimal bd = new BigDecimal(num);  
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}

```