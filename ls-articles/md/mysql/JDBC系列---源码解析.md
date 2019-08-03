---
title: JDBC系列---源码解析
categories: 
- mysql
tags:
---

jdbc源码设计解析

桥接设计模式
动态代理


1、java.sql.Driver接口：功能是从驱动实例对象中拿到具体的Connection实例，
这个链接是关键点，后面的其他接口直接或者间接依赖这个驱动返回的链接的实现类；
2、java.sql.Connection接口：功能是拿到具体的Statement实例
3、java.sql.Statement和java.sql.PreparedStatement接口：功能是拿到具体的ResultSet结果实例
4、java.sql.ResultSet

```java
public interface Driver {
	 Connection connect(String url, java.util.Properties info) throws SQLException;  //核心方法

}
```

mysql实现的驱动
```java
public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }

    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
```


# Driver接口
[通过Driver获取的connection连接是一个Tcp链接]

每个具体的数据库连接驱动类必须实现这个接口，DriverManager在启动的时候，通过[SPI机制，JDBC4.0以后的功能，可以不使用Class.forName装载驱动]，
ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
加载每个jar包下的META-INF/services下文件名java.sql.Driver文件，一般每个文件只有一行（多个写多行即可），内容为该驱动类的实现的全限定名
如：
MySQL 5.1.38有一下两行
com.mysql.jdbc.Driver
com.mysql.fabric.jdbc.FabricMySQLDriver
ClickHouse 0.1.34有下面一行
ru.yandex.clickhouse.ClickHouseDriver
Presto 0.132有下面一行
com.facebook.presto.jdbc.PrestoDriver
这样会加载到系统中引入的所有驱动类的实例，DriverManager内部维护一个[CopyOnWriteArrayList]保存所有驱动类，然后在获取connection的时候
会遍历这个list中所有驱动类，找到合适的驱动类根据URL获得connection，在遍历的过程中通过不匹配的返回null来跳过。


当一个驱动类加载的时候，它应该把它自己注册到DriverManager中，这样就可以实现，通过Class.forName("foo.bah.Driver")这样的方式，加载和注册。
同时，也会创建一个DriverAction，当DriverManager注销驱动类的时候会通知DriverAction。

public interface Driver {

	1、当使用不匹配的驱动获得链接的时候，建议返回null，这样在DriverManager在遍历驱动找到合适的去创建连接，不匹配的统一返回null，跳过；
	2、当驱动类和目标URL匹配，但是在连接数据库的时候出现问题的时候，抛出SQLException异常；
	3、连接数据库的配置可以通过Properties来实现，至少包含用户名和密码，如何一个属性在URL和Properties都出现，则系统会根据实际情况来选择，
	但是不建议一个属性值在两个地方都设置。
    Connection connect(String url, java.util.Properties info) throws SQLException;

    判断目标URL和驱动支持的协议是否匹配，匹配的话返回true，否则返回false
    boolean acceptsURL(String url) throws SQLException;

    链接数据库需要的参数
    DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException;

    获取驱动的主版本号，初始化为1，MySQL的标准实现为5；
    int getMajorVersion();

    获取驱动的次版本号，初始化为0，MySQL的标准实现为1；
    int getMinorVersion();

    来标识该驱动类的实现是不是标准的驱动类，支持所有的数据库，则为true，否则为false
    （一般的实现都是false，MySQL的标准实现就是false，应该没有一个驱动类的实现可以满足链接所有的数据库吧）
    boolean jdbcCompliant();

   	获取日志类（MySQL的标准实现没有这个方法）
    public Logger getParentLogger() throws SQLFeatureNotSupportedException;
}


## NonRegisteringDriver类[MySQL货物connection的关键类]
```java
public class NonRegisteringDriver implements Driver {

 public NonRegisteringDriver() throws SQLException { }  //构造函数

 public static String getOSName() ;     //获取系统的类型，如Windows 7
 public static String getPlatform();    //获取系统的类型，32位还是64位，如amd64
 static int getMajorVersionInternal();  //主版本
 static int getMinorVersionInternal(); //此版本

 protected static String[] parseHostPortPair(String hostPortPair) throws SQLException {}

 //解析URL
 //1、判断URL是否和驱动类匹配,不匹配返回null
public Properties parseURL(String url, Properties defaults) throws SQLException {}   //重要

//获取数据库真实的Connection
public Connection connect(String url, Properties info) throws SQLException {}

//创建负载均衡的连接
  private Connection connectLoadBalanced(String url, Properties info) throws SQLException {
        Properties parsedProps = this.parseURL(url, info);
        if (parsedProps == null) {
            return null;
        } else {
            parsedProps.remove("roundRobinLoadBalance");   //轮流负载均衡
            int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));  //负载均衡的主机个数
            List<String> hostList = new ArrayList();

            for(int i = 0; i < numHosts; ++i) {
                int index = i + 1;
                hostList.add(parsedProps.getProperty("HOST." + index) + ":" + parsedProps.getProperty("PORT." + index));
            }

            return LoadBalancedConnectionProxy.createProxyInstance(hostList, parsedProps);
        }
    }


//创建主从结构的连接
 protected Connection connectReplicationConnection(String url, Properties info) throws SQLException {
        Properties parsedProps = this.parseURL(url, info);
        if (parsedProps == null) {
            return null;
        } else {
            Properties masterProps = (Properties)parsedProps.clone();
            Properties slavesProps = (Properties)parsedProps.clone();
            slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
            int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
            if (numHosts < 2) {   //注解的个数必须大于2
                throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00", (ExceptionInterceptor)null);
            } else {
                List<String> slaveHostList = new ArrayList();
                List<String> masterHostList = new ArrayList();
                String firstHost = masterProps.getProperty("HOST.1") + ":" + masterProps.getProperty("PORT.1");
                boolean usesExplicitServerType = isHostPropertiesList(firstHost);

                for(int i = 0; i < numHosts; ++i) {
                    int index = i + 1;
                    masterProps.remove("HOST." + index);
                    masterProps.remove("PORT." + index);
                    slavesProps.remove("HOST." + index);
                    slavesProps.remove("PORT." + index);
                    String host = parsedProps.getProperty("HOST." + index);
                    String port = parsedProps.getProperty("PORT." + index);
                    if (usesExplicitServerType) {
                        if (this.isHostMaster(host)) {
                            masterHostList.add(host);
                        } else {
                            slaveHostList.add(host);
                        }
                    } else if (i == 0) {
                        masterHostList.add(host + ":" + port);
                    } else {
                        slaveHostList.add(host + ":" + port);
                    }
                }

                slavesProps.remove("NUM_HOSTS");
                masterProps.remove("NUM_HOSTS");
                masterProps.remove("HOST");
                masterProps.remove("PORT");
                slavesProps.remove("HOST");
                slavesProps.remove("PORT");
                return ReplicationConnectionProxy.createProxyInstance(masterHostList, masterProps, slaveHostList, slavesProps);
            }
        }
    }




// NUM_HOSTS属性不为1的时候，
 private Connection connectFailover(String url, Properties info) throws SQLException {
        Properties parsedProps = this.parseURL(url, info);
        if (parsedProps == null) {
            return null;
        } else {
            parsedProps.remove("roundRobinLoadBalance");
            int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
            List<String> hostList = new ArrayList();

            for(int i = 0; i < numHosts; ++i) {
                int index = i + 1;
                hostList.add(parsedProps.getProperty("HOST." + index) + ":" + parsedProps.getProperty("PORT." + index));
            }

            return FailoverConnectionProxy.createProxyInstance(hostList, parsedProps);
        }
    }
    
     //做的主要事情是，根据parseURL解析得到的Properties，来把host、user、pwd必须的；port、db非必须的放到数组中
     public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {}
    
    }
```

