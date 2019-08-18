package demo.hivejdbc;

/**
 * @program: all
 * @author: lishuai
 * @create: 2019-08-06 11:58
 */
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 启动server2服务：
 * hive --service hiveserver2 &
 *
 * 可以跑跑通
 */

public class HiveTest {
    private static String driveName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://192.168.254.101:10000/default";
    //  private static String url = "jdbc:hive2://192.168.1.131:10000/test";
    private static String user = "root";
    private static String passwd = "d010";//密码可以不要
    private static String sql = "";
    private static String sql1 = "";
    private static ResultSet res;

    public static void main(String[] args) {
        Connection con = null;
        Statement stm = null;

        try {
            con = getConnection();
            stm = con.createStatement();

            String tableName = "stu1";
            dropTable(stm, tableName);
            createTable(stm, tableName);
            selectData(stm, tableName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(driveName + " not found! ");
            System.out.println(e.getMessage());
        } catch (SQLException e1) {
            e1.printStackTrace();
            System.out.println("connection error! ");
            System.out.println(e1.getMessage());
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
                if (stm != null) {
                    stm.close();
                    stm = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
                System.out.println("close connection or statement error! ");
                System.out.println(e2.getMessage());
            }
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driveName);
        Connection con = DriverManager.getConnection(url, user, passwd);
        System.out.println("connection success!");
        return con;
    }

    private static void dropTable(Statement stm, String tableName) throws SQLException {
        sql = "drop table if exists " + tableName;
        System.out.println("Running:" + sql);
        stm.executeUpdate(sql);
    }

    private static void createTable(Statement stm, String tableName) throws SQLException {
        sql = "create table if not exists " + tableName + " (stuid string, name string, sex string, age int) clustered by (stuid) into 2 buckets STORED AS ORC";
        System.out.println("Running:" + sql);
        stm.executeUpdate(sql);
        sql1 = "insert into "+tableName+"(stuid,name,sex,age) values ('1001','xubin1','man',25),('1002','xubin2','man',26),('1003','xubin3','man',27),('1004','xubin4','man',28)";
        stm.executeUpdate(sql1);
        String id,name,gender,num;
        try {
            id = new String("1001".getBytes(),"iso8859-1");
            name = new String("徐彬1".getBytes(),"iso8859-1");
            gender = new String("男".getBytes(),"iso8859-1");
            num = "25";
            sql1 = "insert into stu1 values('$ID','$NAME','$GENDER',$NUM)";
            stm.execute(sql1.replace("$ID", id).replace("$NAME", name).replace("$GENDER", gender).replace("$NUM", num));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private static void selectData(Statement stm, String tableName) throws SQLException {
        sql = "select * from " + tableName;
        System.out.println("Running:" + sql);
        res = stm.executeQuery(sql);
        while (res.next()) {
            String uid = res.getString(1);
            String ufname = res.getString(2);
            String ulname = res.getString(3);
            String udate = res.getString(4);
            System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );
        }
    }



}

