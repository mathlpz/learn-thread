package com.lpz.mysql.test3.object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lpz.mysql.test3ok.DButil;
import com.lpz.mysql.test3ok.User;

public class jdbcconnection {

	public static void main(String[] args) {
		// insert("yangxu","yangxu@qq.com");
		// Customer c=new Customer();
		//// c.setName("zhangbing");
		//// c.setEmail("zhangbing@qq.com");
		// //insert(c);
		// c.setId(1001);
		// c.setName("kaixin");
		// Update(c);
		// delete(1006);
		User c = query(11);
		System.out.println(c.getId() + "," + c.getPhone());
	}

	static void insert(Customer c) {
		String sql = "insert into Haige(name,email) value(?,?)";
		Connection conn = DButil.open();
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, c.getName());
			pstmt.setString(2, c.getEmail());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButil.close(conn);
		}

	}

	static void Update(Customer c) {
		String sql = "update haige set name=? where id=?";
		Connection conn = DButil.open();
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, c.getName());
			pstmt.setInt(2, c.getId());
			;
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButil.close(conn);
		}

	}

	static void delete(int id) {
		String sql = "delete from haige where id=?";
		Connection conn = DButil.open();
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			;
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButil.close(conn);
		}
	}

	static User query(int id) {
		String sql = "select * from user where id=?";
		Connection conn = DButil.open();
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int it = rs.getInt(1);
				String phone = rs.getString(2);
				User c = new User();
				c.setId(id);
				c.setPhone(phone);
				return c;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DButil.close(conn, pstmt);
		}
		return null;
	}
}
