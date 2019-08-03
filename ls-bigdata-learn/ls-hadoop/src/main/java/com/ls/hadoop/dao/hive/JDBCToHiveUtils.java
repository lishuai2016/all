package com.ls.hadoop.dao.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCToHiveUtils {
    private static String driverName ="org.apache.hive.jdbc.HiveDriver";
    private static String Url="jdbc:hive2://192.168.137.127:10000/default";    //填写hive的IP，之前在配置文件中配置的IP
    private static Connection conn;
    public static Connection getConnnection()
    {
        try
               {
                  Class.forName(driverName);
                  conn = DriverManager.getConnection(Url, "root", "111111");        //此处的用户名一定是有权限操作HDFS的用户，否则程序会提示"permission deny"异常
               }
        catch(ClassNotFoundException e)  {
                   e.printStackTrace();
                   System.exit(1);
                }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static PreparedStatement prepare(Connection conn, String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
}