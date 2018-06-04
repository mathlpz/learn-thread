package com.lpz.mysql;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpz.mysql.test3ok.JdbcConnection;
import com.lpz.mysql.test3ok.User;
import com.lpz.utils.StringUtil;

/**
 * 整理数据库中的数据。。。执行删除命令一定要谨慎！！！
 * 一些SQL：
 * 		SELECT phone FROM user_che WHERE CHAR_LENGTH(TRIM(phone)) < 11;
 * 		SELECT phone FROM user_che WHERE CHAR_LENGTH(phone) > 11;
 * 		DELETE FROM user_che WHERE CHAR_LENGTH(TRIM(phone)) < 11;
 * 
 * csv文件处理流程：
 * 1、读取csv文件的数据到数据库（>=11位字符）；
 * 		user_che:159267 = 161078-1811
 * 		user_fang:199269 = 213611-14342
 * 	select count(1) from user_che;
 * 	select count(1) from (Select MIN(id) From user_che Group By phone) t;
	Delete FROM user_che Where id NOT IN ( 
		SELECT id from (Select MIN(id) AS id From user_che Group By phone) temp 
	);
		二合一：355981 = 358536 - 2555
 * 2、处理非1开头、非数字结尾的字符串；
 * 	处理大于11位的字符串，update or insert table: 
 * 		354555 = 357939 - 3384...|...372359-14408... 
 * 3、再次处理开头、结尾...354458
 * 
 * @author lpz
 *
 */
public class ProcessDataUtil {

	private final static Logger logger = LoggerFactory.getLogger(ProcessDataUtil.class);
	
	public static String tableName = "user_che_fang_csv_bak";
	
	public static int userListSize = 30000;
	public static int phoneListSize = 20000;
	
	
	public static void main(String[] args) {
		System.out.println("main...");
	}
	
	/**
	 * 1
	 */
	@Test
	public void dealWithUserTest() {
		logger.info("dealWithUserTest...");
		
		long startTime = System.currentTimeMillis();
		new ProcessDataUtil().dealWithUser();
		
		long endTime = System.currentTimeMillis();
		logger.info("dealWithCSVUserTest 用时：{}ms-----------------------", (endTime - startTime));
		
	}
	
	/**
	 * 2
	 */
	@Test
	public void updateAndInsertUserTest() {
		logger.info("updateAndInsertUserTest...");
		
		long startTime = System.currentTimeMillis();
		new ProcessDataUtil().updateAndInsertUser();
		
		long endTime = System.currentTimeMillis();
		logger.info("dealWithCSVUserTest 用时：{}ms-----------------------", (endTime - startTime));
		
	}
	
	/**
	 * 3、删除非手机号数据
	 */
	@Test
	public void deleteNotBobilePhoneTest() {
		logger.info("deleteNotBobilePhoneTest...");
		
		long startTime = System.currentTimeMillis();
		new ProcessDataUtil().deleteNotMobileBatch();
		
		long endTime = System.currentTimeMillis();
		logger.info("deleteNotBobilePhoneTest 用时：{}ms-----------------------", (endTime - startTime));
	}
	
	
	/**
	 * 处理更新数据杂乱格式
	 */
	private void dealWithUser() {

		List<User> userNBList = JdbcConnection.queryUser(tableName);
		logger.info("dealWithCSVUser queryUser user size:" + userNBList.size());
		
		List<User> toUpdateUserList = new ArrayList<User>(userListSize);
		
		for (User user : userNBList) {
			String phone = user.getPhone();
			
//			phone = StringUtil.removeChineseStr(phone);
			int length = phone.length();
			if (phone.length() != 11) {
				logger.warn(user.getId()+ "---------" + user.getPhone());
			}
			
			// 处理非1开头的字符，去除首位
//			if (!phone.startsWith("1")) {
//				logger.info("startsWith(1):" + user.getId()+ "---------" + user.getPhone());
//				String phoneTmp = phone.substring(1).trim();
//				user.setPhone(phoneTmp);
//				toUpdateUserList.add(user);
//			}
			// 处理非数字结束的字符串，去除末尾
//			if (!Character.isDigit(phone.charAt(length-1))) {
//				logger.info("end isDigit(1):" + user.getId()+ "---------" + user.getPhone());
//				phone = phone.substring(0, length-1).trim();
//				user.setPhone(phone);
//				toUpdateUserList.add(user);
//			}
			
			// 最后处理数据时最好考虑到，号码中间分隔符等，替换处理后再执行updateAndInsertCSVUser操作!!!
			// 如：1,588,319,0，130/1398181，1|||1398180，135,415,630
			// phone = phone.replace(",", "");
			
			
		}
		
//		JdbcConnection.updateBatch(toUpdateUserList, tableName);
		
	}
	
	
	/**
	 * 截取更新插入单行多数据的情况
	 */
	private void updateAndInsertUser() {

		List<User> userNBList = JdbcConnection.queryUser(tableName);
		logger.info("updateAndInsertCSVUser queryUser user size:" + userNBList.size());
		
		List<User> toUpdateUserList = new ArrayList<User>(userListSize);
		
		List<String> insertPhoneList = new ArrayList<String>(phoneListSize);
		
		for (User user : userNBList) {
			String phone = user.getPhone();
			
			// 多位字符串
			if (phone.length() > 11) {
				// 拆分成多部分，首部分更新！
				String substr1 = phone.substring(0, 11);
				user.setPhone(substr1);
				toUpdateUserList.add(user);
				// 剩余部分
				String substr2 = phone.substring(11, phone.length()).trim();
				if (substr2.length() < 11) {
					// 丢弃
					logger.info("长度小于11，丢弃:" + substr2 + ", phone:" + phone);
				} else {
					// >=11 第二部分插入
					insertPhoneList.add(substr2);
				}
			}
		}
		
		JdbcConnection.insertBatch(insertPhoneList, tableName);

		JdbcConnection.updateBatch(toUpdateUserList, tableName);
		
	}
	
	/**
	 * 
	 */
	private void deleteNotMobileBatch(){
		List<User> userNBList = JdbcConnection.queryUser(tableName);
		logger.info("deleteNotMobileBatch queryUser user size:" + userNBList.size());
		
		List<User> toDeleteUserList = new ArrayList<User>(userListSize);
		for (User user : userNBList) {
			String phone = user.getPhone();
			
			if (!StringUtil.isMobileNum(phone)) {
				logger.warn(user.getId()+ "---------" + user.getPhone());
				toDeleteUserList.add(user);
			}
		}
		
		logger.error("not mobile phone num:{}", toDeleteUserList.size());
		
		JdbcConnection.deleteBatch(toDeleteUserList, tableName);
		
	}
	
	
	
}
