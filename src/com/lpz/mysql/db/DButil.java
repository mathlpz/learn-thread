package com.lpz.mysql.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author lpz
 *
 */
public class DButil {
	
	
	private final static Logger logger = LoggerFactory.getLogger(DButil.class);
	
	/*
	 * 打开数据库
	 */
	private static String driver;// 连接数据库的驱动
	private static String url;
	private static String username;
	private static String password;
//	private static String dbip = "172.28.16.64";
	private static String dbip = "127.0.0.1";
	private static String dbname = "testdb";

	static {
//		driver = "com.mysql.jdbc.Driver";// 需要的数据库驱动
		driver = "com.mysql.cj.jdbc.Driver";// 需要的数据库驱动
		url = "jdbc:mysql://" + dbip + ":3306/" + dbname + "?serverTimezone=UTC&allowMultiQueries=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false";// 数据库名路径
		username = "root";
		password = "123456";
	}

	public static Connection open() {
		try {
			Class.forName(driver);
			logger.info("~~~dbip:{}~~~dbname:{}", dbip, dbname);
			return (Connection) DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("数据库连接失败！" + e.getMessage(), e);
		} // 加载驱动
		return null;
	}
	
	
	/**
	 * test1
	 * 最常见的ThreadLocal使用场景为 用来解决 数据库连接、Session管理等。
	 */
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
		public Connection initialValue() {
		    try {
				return DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    return null;
		}
	};
	public static Connection getConnection() {
		return connectionHolder.get();
	}
//	private static final ThreadLocal threadSession = new ThreadLocal();
//	public static Session getSession() throws InfrastructureException {
//	    Session s = (Session) threadSession.get();
//	    try {
//	        if (s == null) {
//	            s = getSessionFactory().openSession();
//	            threadSession.set(s);
//	        }
//	    } catch (HibernateException ex) {
//	        throw new InfrastructureException(ex);
//	    }
//	    return s;
//	}


	/*
	 * 关闭数据库
	 */
	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
	
	/*
	 * 关闭数据库
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		}
	}

}
