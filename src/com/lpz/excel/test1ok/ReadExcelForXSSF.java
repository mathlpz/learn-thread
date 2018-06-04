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
import com.lpz.utils.StringUtil;

/**
 * 评估3处理流程：
 * 1、通过读取分成两波数据；一波纯号码、一波杂乱数据（同时读入同时处理）
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
	
	// excel中第几个sheet
	public static int sheetInt = 0;
	
	
//	public static String fileNamePath = "g:/test.xlsx";
	public static String directoryPath = "G:/42excel/fang";
	
//	public static String tableName = "user_excel_che";
	public static String tableName = "user_excel_fang";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		logger.info("ReadExcelForXSSF...");
		
	}
	
	/**
	 * 按照文件夹批量处理！
	 */
	@Test
	public void readAndInsertExcelBatchTest() {
		
		ReadExcelForXSSF readExcelForXSSF = new ReadExcelForXSSF();
		
		long startTime = System.currentTimeMillis();
		// 读取文件
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			logger.error("directoryPath not exist!!!" + directoryPath);
            return ;
        }
		File[] files = baseFile.listFiles();
		logger.info("~~directoryPath:{}, listFiles size:{}", directoryPath, files.length);
		
		for (File fileName:files) {
			String absolutePath = fileName.getAbsolutePath();
			readExcelForXSSF.dealSingleFile(absolutePath);
		}
		
		long endTime = System.currentTimeMillis();  
		logger.info("readExcel and insert 用时：{}ms-------------------------------directoryPath:{}", (endTime - startTime), directoryPath);
	}
	
	/**
	 * 单个处理文件！
	 */
	@Test
	public void readAndInsertExcelSingleTest() {
		String fileAbsolutePath = "g:/test.xlsx";
		new ReadExcelForXSSF().dealSingleFile(fileAbsolutePath);
	}
	
	
	
	/**
	 * 
	 * @param fileAbsolutePath 文件绝对路径
	 */
	private void dealSingleFile(String fileAbsolutePath) {
		// 读取数据
		long startTime = System.currentTimeMillis(); 
		
		new ReadExcelForXSSF().readAndInsertExcel(fileAbsolutePath);
		
		long endTime = System.currentTimeMillis();  
		logger.info("readExcel and insert 用时：{}ms-------------------------------fileAbsolutePath:{}", (endTime - startTime), fileAbsolutePath);
	}
	
	
	
	/**
	 * 
	 * @param fileAbsolutePath
	 */
	private void readAndInsertExcel(String fileAbsolutePath) {
		// 读取文件
		File file = new File(fileAbsolutePath);
		if (!file.exists()) {
			logger.error("file not exist!!!");
			return;
		}
		logger.info("go to read and insert Excel File: " + fileAbsolutePath);
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
		
		// 组装获取
		List<String> phoneList = acquirePhone(workbook);
		
		JdbcConnection.insertBatch(phoneList, tableName);
		
		
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
	private List<String> acquirePhone(Workbook workbook){
		// list for batch
		List<String> phoneList = new ArrayList<String>(phoneListSize);
		
		// 工作表对象，sheet表单
		Sheet sheet = workbook.getSheetAt(sheetInt);
		int rowLength = sheet.getLastRowNum() + 1; // 总行数
		Row row = sheet.getRow(0); // 工作表的列
		int colLength = row.getLastCellNum(); // 总列数
		Cell cell = row.getCell(0); // 得到指定的单元格
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
			// Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常： Cannot get a STRING value from a NUMERIC cell。 将所有的需要读的Cell表格设置为String格式
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
			
			// 存入数据库
			phoneList.add(stringCellValue);
//			}
		}
		// 
		logger.warn("nullRow:{}, nullCell:{}, nullStrValue:{}", nullRow, nullCell, nullStrValue);
		
		return phoneList;
	}
	
	

}
