package com.lpz.mysql.test3ok;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * 
 * @author lpz
 *
 */
public class DButil {
	/*
	 * 打开数据库
	 */
	private static String driver;// 连接数据库的驱动
	private static String url;
	private static String username;
	private static String password;
	private static String dbip = "172.28.16.64";
//	private static String dbname = "testdb";
	private static String dbname = "testdb2";

	static {
//		driver = "com.mysql.jdbc.Driver";// 需要的数据库驱动
		driver = "com.mysql.cj.jdbc.Driver";// 需要的数据库驱动
		url = "jdbc:mysql://" + dbip + ":3306/" + dbname + "?allowMultiQueries=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";// 数据库名路径
		username = "root";
		password = "123456";
	}

	public static Connection open() {
		try {
			Class.forName(driver);
			System.out.println("~~dbname:" + dbname);
			return (Connection) DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		} // 加载驱动
		return null;
	}

	/*
	 * 关闭数据库
	 */
	public static void close(Connection conn, PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
			}
		}
	}

}
