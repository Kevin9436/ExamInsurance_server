package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";// ����������
    private static final String DBNAME = "examinsurance_db";// ���ݿ���
    private static final String DBCONFIG = "?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT";//�������ݿ�����
    private static final String DBURL = "jdbc:mysql://localhost:3306/" + DBNAME+DBCONFIG;// ����URL
    private static final String DBUSER = "root";// ���ݿ��û���
    private static final String DBPASSWORD = "lzy0225vip9436";// ���ݿ�����
    
    private static Connection conn = null;
    
    //��ȡ���ݿ�����
    public static Connection getConnection() {
    	if(conn==null) {
	        try {
	            Class.forName(DBDRIVER);// ע������
	
	            conn = (Connection) DriverManager.getConnection(DBURL, DBUSER,
	                    DBPASSWORD);// ������Ӷ���
	            System.out.println("�ɹ�����MYSQL��������");
	        } catch (ClassNotFoundException e) {// �����������޷��ҵ��쳣
	            System.out.println("�Ҳ���MYSQL��������");
	            System.out.println(e.toString());
	            e.printStackTrace();
	
	        } catch (SQLException e) {// ����SQL�쳣
	
	            e.printStackTrace();
	        }
    	}
        return conn;
    }
    
    //�ͷ�����
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                System.out.println("���ݿ�ر��쳣��[" + e.getErrorCode() + "]" + e.getMessage());
            }
        }
    }
    
    //��ѯ����
    public static ResultSet query(String querySql) throws SQLException {
        Statement stateMent = (Statement) getConnection().createStatement();
        return stateMent.executeQuery(querySql);
    }
    
    //��ɾ�Ĳ���
    public static int update(String insertSql) throws SQLException {
        Statement stateMent = (Statement) getConnection().createStatement();
        return stateMent.executeUpdate(insertSql);
    }
    
}