package com.ls.jdbc.mysql;

import java.sql.*;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-06 23:53
 */
public class PreparedStatement_Select {


    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String user = "root";
        String pwd = "";
        String sql = "SELECT  * FROM course";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pwd);
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String des = resultSet.getString(3);
                System.out.println(+id+":"+name+":"+des);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
