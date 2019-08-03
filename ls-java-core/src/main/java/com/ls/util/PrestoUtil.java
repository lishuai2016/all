package com.ls.util;

import com.google.common.collect.Lists;
import com.ls.model.TableColumns;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @program: db-parser-component
 * @author: lishuai
 * @create: 2018-12-04 21:54
 * presto不支持的SQL：
 * 1、show databases; 因此必须要选择一个数据库
 *
 */
public class PrestoUtil {
    private static final String Presto_driver = "com.facebook.presto.jdbc.PrestoDriver";


    public static void main(String[] args) {

    }

    /**
     * 获取所有的数据库
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static List<String> queryTables(String url, String username, String password) {
        List<String> databases = Lists.newArrayList();
        JdbcUtil jdbcUtil = new JdbcUtil(Presto_driver,url,username,password);
        Connection conn = jdbcUtil.conn;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("show tables");  //注意不能带分号
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                databases.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.release();
        }
        System.out.println("数据库列表："+databases);
        return databases;
    }


    public static  List<TableColumns> queryColumns(String url, String username, String password , String tableName) {
        List<TableColumns> tableColumns = Lists.newArrayList();
        JdbcUtil jdbcUtil = new JdbcUtil(Presto_driver,url,username,password);
        Connection conn = jdbcUtil.conn;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("desc " + tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String column_name = resultSet.getString(1);
                String columntype = resultSet.getString(2);
                TableColumns Columns = new TableColumns();
                Columns.setTableName(tableName);
                Columns.setColumnName(column_name);
                Columns.setDataType(columntype);
                tableColumns.add(Columns);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.release();
        }
        System.out.println("数据表的列："+tableColumns);

        return tableColumns;
    }

}
