package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";// 驱动类类名
    private static final String DBNAME = "examinsurance_db";// 数据库名
    private static final String DBCONFIG = "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT";//配置数据库连接
    private static final String DBURL = "jdbc:mysql://localhost:3306/" + DBNAME+DBCONFIG;// 连接URL
    private static final String DBUSER = "root";// 数据库用户名
    private static final String DBPASSWORD = "lzy0225vip9436";// 数据库密码
    
    private static Connection conn = null;
    
    //获取数据库连接
    public static Connection getConnection() {
    	if(conn==null) {
	        try {
	            Class.forName(DBDRIVER);// 注册驱动
	
	            conn = (Connection) DriverManager.getConnection(DBURL, DBUSER,
	                    DBPASSWORD);// 获得连接对象
	            System.out.println("成功加载MYSQL驱动程序");
	        } catch (ClassNotFoundException e) {// 捕获驱动类无法找到异常
	            System.out.println("找不到MYSQL驱动程序");
	            System.out.println(e.toString());
	            e.printStackTrace();
	
	        } catch (SQLException e) {// 捕获SQL异常
	
	            e.printStackTrace();
	        }
    	}
        return conn;
    }
    
    //释放连接
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                System.out.println("数据库关闭异常：[" + e.getErrorCode() + "]" + e.getMessage());
            }
        }
    }
    
    //查询操作
    public static ResultSet query(String querySql) throws SQLException {
        Statement stateMent = (Statement) getConnection().createStatement();
        return stateMent.executeQuery(querySql);
    }
    
    //增删改操作
    public static int update(String insertSql) throws SQLException {
        Statement stateMent = (Statement) getConnection().createStatement();
        return stateMent.executeUpdate(insertSql);
    }
    
}