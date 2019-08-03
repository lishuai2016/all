package com.ls.util;

import com.google.common.collect.Lists;
import com.ls.model.Table;
import com.ls.model.TableColumns;


import java.sql.*;
import java.util.List;

/**
 * @program: db-parser-component
 * @author: lishuai
 * @create: 2018-12-04
 * MySQL和memsql通用
 * 他们的服务器都存有表元数据信息的数据库information_schema，
 * 通过该库下的表tables获取表相关信息；通过columns获取一张表的结构信息
 */
public class MysqlUtil {
    private static final String driver = "com.mysql.jdbc.Driver";
    public static void main(String[] args) {

    }

    /** 获取指定数据库下的所有
     * @param url   URL串需要包含所选的数据库
     * @param username
     * @param password
     * @return
     */
    public static List<Table> queryTables(String url, String username, String password) {
        List<Table> tables = Lists.newArrayList();
        JdbcUtil jdbcUtil = new JdbcUtil(driver,url,username,password);
        Connection conn = jdbcUtil.conn;
        StringBuilder sql = new StringBuilder();
        sql.append(" select table_name, engine, table_comment , create_time  from information_schema.tables")
                .append(" where table_schema = (select database())")
                .append(" order by create_time desc");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String table_name=resultSet.getString(1);
                String engine=resultSet.getString(2);
                String table_comment=resultSet.getString(3);
                Date create_time = resultSet.getDate(4);
                //System.out.println(table_name+":"+engine+":"+table_comment+":"+date);
                Table table = new Table();
                table.setTableName(table_name);
                table.setEngine(engine);
                table.setTableComment(table_comment);
                table.setCreateTime(create_time);
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            jdbcUtil.release();
        }
        System.out.println("数据表："+tables);
        return tables;
    }

    /**
     * 获取所有的数据库
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static  List<String>  queryDatabases( String url, String username, String password) {
        List<String> databases = Lists.newArrayList();
        JdbcUtil jdbcUtil = new JdbcUtil(driver,url,username,password);
        Connection conn = jdbcUtil.conn;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("show databases;");
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


    /**
     * 获取某个表的字段信息
     * @param url
     * @param username
     * @param password
     * @param tableName
     * @return
     */
    public static List<TableColumns> queryColumns(String url, String username, String password , String tableName) {
        List<TableColumns> tableColumns = Lists.newArrayList();
        JdbcUtil jdbcUtil = new JdbcUtil(driver,url,username,password);
        Connection conn = jdbcUtil.conn;
        StringBuilder sql = new StringBuilder();
        sql.append("select table_name,column_name, data_type, column_comment, column_key , extra from information_schema.columns")
                .append(" where table_schema = (select database())")
                .append(" and table_name =").append("'").append(tableName).append("'")   //需要注意的是，tableName需要用单引号括起来
                .append(" order by ordinal_position");
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String table_name=resultSet.getString(1);
                String column_name = resultSet.getString(2);
                String data_type=resultSet.getString(3);
                String column_comment=resultSet.getString(4);
                String column_key=resultSet.getString(5);
                String extra=resultSet.getString(6);
                TableColumns Columns = new TableColumns();
                Columns.setTableName(table_name);
                Columns.setColumnName(column_name);
                Columns.setDataType(data_type);
                Columns.setColumnComment(column_comment);
                Columns.setColumnKey(column_key);
                Columns.setExtra(extra);
                //System.out.println(table_name+":"+column_name+":"+data_type+":"+column_comment+":"+column_key+":"+extra);
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
