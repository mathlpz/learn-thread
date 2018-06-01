package com.lpz.excel.mysql.test1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 直连
 * 
 * @author lpz
 *
 */
public class DbTest {

	public static void main(String[] args) {
		
		// 声明Connection对象
		Connection connection;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:mysql://172.28.16.64:3306/testdb?allowMultiQueries=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123456";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			connection = DriverManager.getConnection(url, user, password);
			if (!connection.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			}
			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = connection.createStatement();
			// 要执行的SQL语句
			String sql = "select * from user";
			// 3.ResultSet类，用来存放获取的结果集！！
			ResultSet rs = statement.executeQuery(sql);
			
			System.out.println("-----------------");
			System.out.println("执行结果如下所示:");
			System.out.println("-----------------");
			System.out.println("姓名" + "\t" + "职称");
			System.out.println("-----------------");

			String job = null;
			String id = null;
			while (rs.next()) {
				// 获取stuname这列数据
				job = rs.getString("name");
				// 获取stuid这列数据
				id = rs.getString("phone");

				// 输出结果
				System.out.println(id + "\t" + job);
			}
			rs.close();
			connection.close();
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry, can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("数据库数据成功获取！！");
		}
	}

}