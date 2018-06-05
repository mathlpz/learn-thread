package com.lpz.mysql.test3ok;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *   （转）今天在做一个将excel数据导入数据库的程序时，由于数据量大，准备采用jdbc的批量插入。于是用了preparedStatement.addBatch();
 *   当加入1w条数据时，再执行插入操作，preparedStatement.executeBatch()。
 *   我原以为这样会很快，结果插入65536条数据一共花30多分钟，完全出乎我的意料。于是问了一下同事，他们在处理这种大批量数据导入的时候是如何处理的，发现他们也是用的jdbc批量插入处理，
 *   但与我不同是：他们使用了con.setAutoCommit(false);然后再preparedStatement.executeBatch()之后，再执行con.commit();
 *   于是再试，什么叫奇迹？就是刚刚导入这些数据花了半小时，而加了这两句话之后，现在只用了15秒钟就完成了。于是去查查了原因，在网上发现了如下一段说明：

    * When importing data into InnoDB, make sure that MySQL does not have autocommit mode enabled 
    * because that requires a log flush to disk for every insert. 
    * To disable autocommit during your import operation, surround it with SET autocommit and COMMIT statements:
    
      SET autocommit=0;
     ... SQL import statements ...
     COMMIT;

  *  第一次，正是因为没有setAutoCommit(false)；那么对于每一条insert语句，都会产生一条log写入磁盘，所以虽然设置了批量插入，但其效果就像单条插入一样，导致插入速度十分缓慢。
  *   
 * @author lpz
 *
 */
public class JdbcConnection {
	
	private final static Logger logger = LoggerFactory.getLogger(JdbcConnection.class);
	
	// 分批插入
	public static int segmentSize = 20000;
	
	public static int queryUserSize = 1100000;

	public static void main(String[] args) {
		insert("yangxu", "yangxu@qq.com");
	}

	/**
	 * 
	 * @param name
	 * @param email
	 */
	public static void insert(String name, String email) {
		String sql = "insert into Haige(name,email) value(?,?)";
		Connection conn = DButil.open();
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			DButil.close(conn);
		}
	}

	/**
	 * 
	 * @param phone
	 */
//	public static void insertUser(String phone) {
//		String sql = "insert into user(phone) value(?)";
//		Connection conn = DButil.open();
//		try {
//			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
//			pstmt.setString(1, phone);
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//	logger.error(e.getMessage(), e);
//		} finally {
//			DButil.close(conn);
//		}
//	}

	/**
	 * 
	 * @param phone
	 */
//	public static void insertUserNB(String phone) {
//		String sql = "insert into usernb(phone) value(?)";
//		Connection conn = DButil.open();
//		try {
//			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
//			pstmt.setString(1, phone);
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//	logger.error(e.getMessage(), e);
//		} finally {
//			DButil.close(conn);
//		}
//	}
	
	/**
	 * 
	 * @param phone
	 */
	public static void insertDB(String phone, String dbName) {
		 
		logger.info("insertDB. phone:" + phone + ", dbTable:" + dbName);
		
//		String sql = "insert into usernb(phone) value(?)";
		String sql = "insert into " + dbName + "(phone) value(?)";
		Connection conn = DButil.open();
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			DButil.close(conn);
		}
	}

	/**
	 * 
	 * @param phoneList
	 */
//	public static void insertUserBatch(List<String> phoneList) {
//		String sql = "insert into user(phone) value(?)";
//		Connection conn = DButil.open();
//		PreparedStatement pstmt = null;
//		try {
//			// 关闭事务自动提交
//			conn.setAutoCommit(false);
//			pstmt = (PreparedStatement) conn.prepareStatement(sql);
//			
//			int num = phoneList.size();
//			System.out.printf("to insertUserBatch size: %s", num);
//			System.out.println();
//			
//			for (int i = 0; i < num; i++) {
//				// 把一个SQL命令加入命令列表
//				pstmt.setString(1, phoneList.get(i));
//				pstmt.addBatch();
//				// 1w条记录插入一次
//			    if (i != 0 && i % segmentSize == 0){
//			    	System.out.println("~~segmentation insertUserBatch: " + i);
//			         // 执行批量更新
//			         pstmt.executeBatch();
//			         // 语句执行完毕，提交本事务
//			         conn.commit();
//			     }
//			}
//			// 最后插入不足1w条的数据
//	        pstmt.executeBatch();
//	        conn.commit();
//	        
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DButil.close(conn, pstmt);
//		}
//	}
	
	/**
	 * 
	 * @param phoneList
	 */
	public static void updateUserBatch(List<User> userList, String dbName) {
		
		logger.info("updateBatch size:::" + userList.size() + ", dbTable:" + dbName);
		
//		String sql = "update usernb set phone = ? where id = ?";
		String sql = "update " + dbName + " set phone = ? where id = ?";
		Connection conn = DButil.open();
		PreparedStatement pstmt = null;
		try {
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			
			for (int i = 0; i <  userList.size(); i++) {
				// 把一个SQL命令加入命令列表
				pstmt.setString(1, userList.get(i).getPhone());
				pstmt.setInt(2, userList.get(i).getId());
				pstmt.addBatch();
				// 1w条记录插入一次
			    if (i != 0 && i % segmentSize == 0){
			    	logger.info("~~segmentation updateUserNBBatch: " + i);
			         // 执行批量更新
			         pstmt.executeBatch();
			         // 语句执行完毕，提交本事务
			         conn.commit();
			     }
			}
			// 最后插入不足1w条的数据
	        pstmt.executeBatch();
	        conn.commit();
	        
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			DButil.close(conn, pstmt);
		}
	}
	
	
	/**
	 * 
	 * @param userList
	 * @param dbName
	 */
	public static void deleteBatch(List<User> userList, String dbName) {
		
		logger.info("deleteBatch size:::" + userList.size() + ", dbTable:" + dbName);
		
		Connection conn = DButil.open();
		PreparedStatement pstmt = null;
		
//		String sql = "delete from table where id in (0"; 
//		for (int i=0; i < userList.size(); i++) { 
//		  sql += "," + userList.get(i).getId();
//		} 
//		sql += ")"; 
//		pstmt = conn.prepareStatement(sql); 
//		pstmt.execute();
		
		// 注意是=，不是in
		String sql = "delete from " + dbName + " where id = ?";
		try {
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			for (int i = 0; i <  userList.size(); i++) {
				// 把一个SQL命令加入命令列表
				pstmt.setInt(1, userList.get(i).getId());
				pstmt.addBatch();
				// 1w条记录插入一次
			    if (i != 0 && i % segmentSize == 0){
			    	logger.info("~~segmentation deleteBatch: " + i);
			         // 执行批量更新
			    	pstmt.executeBatch();
			         // 语句执行完毕，提交本事务
			         conn.commit();
			     }
			}
			// 最后插入不足1w条的数据
	        pstmt.executeBatch();
	        conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			DButil.close(conn, pstmt);
		}
	}

	/**
	 * 
	 * @param phoneList
	 */
//	public static void insertUserNBBatch(List<String> phoneList) {
//		String sql = "insert into usernb(phone) value(?)";
//		Connection conn = DButil.open();
//		PreparedStatement pstmt = null;
//		try {
//			// 关闭事务自动提交
//			conn.setAutoCommit(false);
//			pstmt = (PreparedStatement) conn.prepareStatement(sql);
//
//			int num = phoneList.size();
//			System.out.printf("to insertUserNBBatch size: %s", num);
//			System.out.println();
//			
//			for (int i = 0; i < num; i++) {
//				pstmt.setString(1, phoneList.get(i));
//				// 把一个SQL命令加入命令列表 
//				pstmt.addBatch();
//				// 1w条记录插入一次
//			    if (i != 0 && i % segmentSize == 0){
//			    	System.out.println("~~segmentation insertUserNBBatch: " + i);
//			         // 执行批量更新
//			         pstmt.executeBatch();
//			         // 语句执行完毕，提交本事务
//			         conn.commit();
//			     }
//			}
//			// 执行批量更新
//			pstmt.executeBatch();
//			// 语句执行完毕，提交本事务
//			conn.commit();
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//		} finally {
//			DButil.close(conn, pstmt);
//		}
//	}
	
	
	
	/**
	 * 
	 * @param phoneList
	 */
	public static void insertBatch(List<String> phoneList, String dbName) {
		logger.info("insertBatch size:::" + phoneList.size() + ", dbTable:" + dbName);
		
//		String sql = "insert into usernb(phone) value(?)";
		String sql = "insert into " + dbName + "(phone) value(?)";
		Connection conn = DButil.open();
		PreparedStatement pstmt = null;
		try {
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			pstmt = (PreparedStatement) conn.prepareStatement(sql);

			for (int i = 0; i < phoneList.size(); i++) {
				pstmt.setString(1, phoneList.get(i));
				// 把一个SQL命令加入命令列表 
				pstmt.addBatch();
				// 1w条记录插入一次
			    if (i != 0 && i % segmentSize == 0){
			    	logger.info("~~segmentation insertBatch: " + i);
			         // 执行批量更新
			         pstmt.executeBatch();
			         // 语句执行完毕，提交本事务
			         conn.commit();
			     }
			}
			// 执行批量更新
			pstmt.executeBatch();
			// 语句执行完毕，提交本事务
			conn.commit();
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			DButil.close(conn, pstmt);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<User> queryUser(String dbName) {
		
		logger.info("queryUserNB, dbTable:" + dbName);
		
		Connection conn = DButil.open();
//		String sql = "select * from usernb";
		String sql = "select * from " + dbName;
		List<User> userList = new ArrayList<User>(queryUserSize);
		try {
			Statement pstmt = conn.createStatement();
			ResultSet rs = pstmt.executeQuery(sql);
			while (rs.next()) {
				int id1 = rs.getInt(1);
				String phone = rs.getString(2);
				User user = new User();
				user.setId(id1);
				user.setPhone(phone);
				userList.add(user);
			}
			return userList;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			DButil.close(conn);
		}
		return null;
	}

}
