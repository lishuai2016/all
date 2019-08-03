package com.ls.lishuai.sql;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/22 14:25
 */

import org.apache.commons.beanutils.BeanUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class JdbcDataSourceTest
{
    private static DataSource dataSource;

    private static Connection connection;

    private static PreparedStatement ps;

    private static ResultSet rs;

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();

        //从指定的配置文件加载数据到properties中
        properties.load(JdbcDataSourceTest.class.getClassLoader().getResourceAsStream("db.properties"));

        /**
         * properties其中一条记录是这样的
         * jdbc.datasource=com.mchange.v2.c3p0.ComboPooledDataSource
         * com.mchange.v2.c3p0.ComboPooledDataSource
         * 将前面的jdbc.去掉在重新装入一个Properties中
         */
        Properties dbProperties = new Properties();

        for (Object key : properties.keySet()) {
            String temp = (String) key;

            if (temp.startsWith("jdbc."))
            {
                String name = temp.substring(5);

                dbProperties.put(name, properties.getProperty(temp));

            }
        }

        //列出dbProperties中的所有属性
        dbProperties.list(System.out);

        System.out.println("==============================================");

        //从厂商提供的DataSource实现中加载其对象
//        dataSource = (DataSource) Class.forName("com.mchange.v2.c3p0.ComboPooledDataSource").newInstance();
        dataSource = (DataSource) Class.forName(" org.apache.commons.dbcp.BasicDataSource").newInstance();


        //将dbProperties属性中的内容加入到dataSource对象中
        BeanUtils.populate(dataSource, dbProperties);



        connection = dataSource.getConnection();

        ps = connection.prepareStatement("select * from test");

        rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}
