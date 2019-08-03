---
title: JDBC系列---jdbc典型应用
categories: 
- mysql
tags:
---


# JDBC典型应用
下面是个最简单的使用JDBC取得数据的应用。主要能分成几个步骤，分别是
1.加载特定数据库驱动器实现类，并注册驱动器（Driver会注册到DriverManager中）；[根据SPI机制，这一步可以省略]
2.根据特定的URL，返回可以接受此URL的数据库驱动对象Driver；
3.使用数据库驱动 Driver 创建数据库连接Connection会话；
4.使用 Connection对象创建 用于操作sql的Statement对象；
5.statement对象 执行 sql语句，返回结果ResultSet 对象；
6.处理ResultSet中的结果；
7.关闭连接，释放资源。


典型的代码示例：
```java
 public class DBConnection {
 
	static final String  URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	static final String USER_NAME ="louluan";
	static final String PASSWORD = "123456";
	
	public static void main(String[] args) {
		connectionTest();
	}
	
	public static void connectionTest(){
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			//1.加载类，并注册驱动器（Driver会注册到DriverManager中）
			
			//加载Oracle数据库驱动
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			
			//2.根据特定的URL，返回可以接受此URL的数据库驱动对象
			Driver driver = DriverManager.getDriver(URL);
			Properties props = new Properties();
			props.put("user", USER_NAME);
			props.put("password", PASSWORD);
			
			//3.使用数据库驱动创建数据库连接Connection会话
			connection = driver.connect(URL, props);
			
			//4.获得Statement对象
			statement = connection.createStatement();
			//5.执行 sql语句，返回结果
			resultSet = statement.executeQuery("select * from hr.employees");
			//6.处理结果，取出数据
			while(resultSet.next())
			{
				System.out.println(resultSet.getString(2));
			}
			
			//7.关闭链接，释放资源
		} catch (ClassNotFoundException e) {
			System.out.println("加载Oracle类失败！");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			    //使用完成后管理链接，释放资源，释放顺序应该是： ResultSet ->Statement ->Connection
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
```

![](/images/jdbc典型应用时序图.jpg)