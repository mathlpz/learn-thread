package com.lpz.excel.test1ok;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpz.mysql.test3ok.JdbcConnection;
import com.lpz.mysql.test3ok.User;
import com.lpz.utils.StringUtil;

/**
 * 评估3处理流程：
 * 1、通过读取分成两波数据；一波纯号码、一波杂乱数据
 * 2、删除纯号码的重复记录；(20112 rows in set / 20329 - 217)
 * 		#Select MIN(id) From user Group By phone;
 * 		#Select MIN(id), phone, count(*) From user Group By phone HAVING count(*) > 1;
 * 		select count(1) from (Select MIN(id) From user Group By phone) t;
 * 		select count(1) from user;
  		Delete FROM usernb Where id NOT IN ( 
  			SELECT id from (Select MIN(id) AS id From usernb Group By phone) temp 
  		);		
 * 3、整理杂乱数据中的号码：
 * 		去重：（5885 / 5967 - 82）
 *		去除中文字符 （5787）；
 *		删除长度小于11的数据记录（5787 / 5885 - 98）（ 5774 / -13）：
 *			SELECT phone FROM usernb WHERE CHAR_LENGTH(TRIM(phone)) < 11; 
 *			DELETE FROM usernb WHERE CHAR_LENGTH(TRIM(phone)) < 11;
 *		去重：（ 5651 / 5771 - 120）
 *		程序整理以1开头的手机号，整理非数字结尾的数据；（程序处理）
 *		手动删除非号码的行，删除固话括号等杂乱信息（5771）；
 *		截取前11位，插入11位后的数据；
 *		重新整理数据开头部分分数字情况，删除非数字开头部分等；
 *		手动检查替换修改中间等地方的特殊字符；
 *	4、再次删除重复的记录：
 * 	
 * 评估1处理流程：
 * 1、读入Excel字段；
 * 2、去除中文汉字空格；
 * 3-1、删除空记录：（-88 = 8049）
 * 		DELETE FROM `user` WHERE phone = "";
 * 3-2、删除长度小于11位的记录：（-43 = 8006）
 * 		DELETE FROM user WHERE CHAR_LENGTH(TRIM(phone)) < 11;
 * 4、整理规范，删除头部尾部多余字符：删除首尾非数字字符；（程序处理）；
 * 5、程序整理非1开头的手机号；
 * 5、手动检查删除固话等无用号码，及中间特殊字符；
 * 6、删除重复的记录：（7329 / -672）
 * 		Delete FROM user Where id NOT IN ( 
  			SELECT id from (Select MIN(id) AS id From user Group By phone) temp 
  		);
 * 
 * @author lpz
 *
 */
public class ReadExcelForXSSF {
	
	private final static Logger logger = LoggerFactory.getLogger(ReadExcelForXSSF.class);

	public static int phoneListSize = 30000;
	public static int phoneListNBSize = 10000;
	
	
//	public static String fileNamePath = "g:/test.xlsx";
	public static String fileNamePath = "g:/评估1.xlsx";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		logger.info("ReadExcelForXSSF...");
		
		
//		System.out.println(removeChineseStr("   sdf发AA三大赛的  发生地方生8 88   "));
	}
	
	/**
	 * 
	 */
	@Test
	public void readAndInsertExcelTest() {
		// 读取数据
//		long startTime = System.currentTimeMillis();  
//		new ReadExcelForXSSF().readAndInsertExcel();
//		
//		long endTime = System.currentTimeMillis();  
//        System.out.printf("readExcel and insert 用时：%sms-------------------------------", (endTime - startTime));  
//        System.out.println();
//		logger.info("readExcel and insert 用时：{}ms-------------------------------fileNamePath:{}", (endTime - startTime), fileNamePath);
        
	}
	
	/**
	 * 
	 */
	@Test
	public void dealWithUserNBTest() {
		  // 处理数据
        long startTime = System.currentTimeMillis();  
        
        new ReadExcelForXSSF().dealWithUserNB();
        
        long endTime = System.currentTimeMillis();  
        System.out.printf("dealWithUserNB 用时：%sms-----------------------", (endTime - startTime));  
        System.out.println();
        logger.info("dealWithUserNB 用时：{}ms-----------------------fileNamePath:{}", (endTime - startTime), fileNamePath);  
		
	}
	
	
	public void dealWithUserNB() {
		
//		String tblName = "usernb2";
		String tblName = "user";
		
		List<User> userNBList = JdbcConnection.queryUser(tblName);
		logger.info("dealWithUserNB queryUser user size:" + userNBList.size());
		
		List<User> toUpdateUserNBList = new ArrayList<User>();
		
		List<String> insertPhoneNB2List = new ArrayList<String>(phoneListNBSize);
		for (User user : userNBList) {
			String phone = user.getPhone();
//			phone = StringUtil.removeChineseStr(phone);
//			phone = phone.replace("：", "");
//			phone = phone.replace("、", "");
//			phone = phone.replace("/", "");
//			phone = phone.replace("  ", "");
//			phone = phone.replace("、", "");
//			if (!phone.startsWith("1")) {
//				logger.info(user.getId()+ "---------" + user.getPhone());
//				String phoneTmp = phone.substring(1).trim();
//				user.setPhone(phoneTmp);
//				logger.info(user.getId()+ "----====-----" + user.getPhone());
//				toUpdateUserNBList.add(user);
//			}
			
			
			// 去除末尾非数字
			int length = phone.length();
			if (length != 11) {
				logger.warn(user.getId()+ "---------" + user.getPhone());
			}
			
//			if (!Character.isDigit(phone.charAt(length-1))) {
//				logger.info(user.getId()+ "---------" + user.getPhone());
//				user.setPhone(phone.substring(0, 11));
//				toUpdateUserNBList.add(user);
//			}
			
			
//			if (phone.length() > 11) {
//				// 拆分成多部分，首部分更新
//				String substr1 = phone.substring(0, 11);
//				user.setPhone(substr1);
//				toUpdateUserNBList.add(user);
//				// 剩余部分
//				String substr2 = phone.substring(11, phone.length()).trim();
//				if (substr2.length() < 11) {
//					// 丢弃
//					logger.info("长度小于11，丢弃:" + substr2 + ", phone:" + phone);
//				} else {
//					// >=11 第二部分插入
//					insertPhoneNB2List.add(substr2);
//				}
//			} 
			
		}
		
		
//		JdbcConnection.insertBatch(insertPhoneNB2List, tblName);
//		
//		JdbcConnection.updateBatch(toUpdateUserNBList, tblName);
		
		
	}
	
	
	
	/**
	 * 
	 */
	public void readAndInsertExcel() {
		// 读取文件
		File file = new File(fileNamePath);
		if (!file.exists()) {
			logger.info("file not exist!!!");
			return;
		}
		// 解析Excel
		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
		// list for batch
		List<String> phoneList = new ArrayList<String>(phoneListSize);
//		List<String> phoneNBList = new ArrayList<String>(phoneListNBSize);
		// 组装获取
		acquirePhone(workbook, phoneList, null);
		
		
		JdbcConnection.insertBatch(phoneList, "user");
		
//		JdbcConnection.insertBatch(phoneNBList, "usernb");

//		// 将修改好的数据保存
//		OutputStream out = new FileOutputStream(file);
//		workbook.write(out);
		
	}
	
	/**
	 * 
	 * @param rowLength
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param nullRow
	 * @param nullCell
	 * @param nullStrValue
	 * @param phoneList
	 * @param phoneNBList
	 */
	private void acquirePhone(Workbook workbook, List<String> phoneList, List<String> phoneNBList){
		// 工作表对象，sheet表单
		Sheet sheet = workbook.getSheetAt(11);
		// 总行数
		int rowLength = sheet.getLastRowNum() + 1;
		// 工作表的列
		Row row = sheet.getRow(0);
		// 总列数
		int colLength = row.getLastCellNum();
		// 得到指定的单元格
		Cell cell = row.getCell(0);
		// 得到单元格样式
//		CellStyle cellStyle = cell.getCellStyle();
		logger.info("行数：" + rowLength + ", 列数：" + colLength);
		
		int nullRow = 0;
		int nullCell = 0;
		int nullStrValue = 0;
		for (int i = 0; i < rowLength; i++) {
			row = sheet.getRow(i);
			// 只有一列数据
//			for (int j = 0; j < colLength; j++) {
			// 如果为空行，则跳过
			if (null == row) {
				nullRow++ ;
				continue;
			}
			cell = row.getCell(0);
			// Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常： Cannot get a STRING value from a NUMERIC cell
			// 将所有的需要读的Cell表格设置为String格式
			if (cell != null) {
				cell.setCellType(CellType.STRING);
			} else {
				nullCell++;
				// 如果为空列，则跳过
				continue;
			}
			// 获取值
			String stringCellValue = cell.getStringCellValue().trim();
//			logger.info(stringCellValue + "----" + i);
			if (StringUtil.isEmpty(stringCellValue)) {
				nullStrValue++;
				continue;
			}
			
			phoneList.add(stringCellValue);
			// 存入数据库
//			if (isMobileNum(stringCellValue)) {
//				phoneList.add(stringCellValue);
//			} else {
//				phoneNBList.add(stringCellValue);
//			}
//			}
		}
		// 
		System.out.printf("nullRow:%s, nullCell:%s, nullStrValue:%s", nullRow, nullCell, nullStrValue);
		System.out.println();
		logger.warn("nullRow:%s, nullCell:{}, nullStrValue:{}", nullRow, nullCell, nullStrValue);
	}
	
	

}
