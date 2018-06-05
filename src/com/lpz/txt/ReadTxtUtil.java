package com.lpz.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lpz.mysql.test3ok.JdbcConnection;
import com.lpz.utils.StringUtil;

/**
 * 读取txt数据
 * 
 * @author lpz
 *
 */
public class ReadTxtUtil {

	private final static Logger logger = LoggerFactory.getLogger(ReadTxtUtil.class);

	public static String dbTable = "user_txt";

	// file name
	public static String directoryPath = "g:/43word";
	
	public static int phoneListSize = 20000;

	/**
	 * 批量文件夹操作
	 */
	@Test
	public void insertDataFromTxtBatchTest() {
		ReadTxtUtil txtUtil = new ReadTxtUtil();
		// 读取文件
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			logger.error("directoryPath not exist!!!");
			return;
		}
		File[] files = baseFile.listFiles();
		logger.info("~~directoryPath:{}, listFiles size:{}", directoryPath, files.length);

		long startTime = System.currentTimeMillis();
		for (File fileName : files) {
			String filePath = fileName.getAbsolutePath();
			txtUtil.insertDataFromTxt(filePath);
		}
		long endTime = System.currentTimeMillis();
		logger.info("insertDataFromTxtBatchTest 用时：{}ms-----------------------directoryPath:{}", (endTime - startTime),
				directoryPath);
	}

	/**
	 * 单个读入操作
	 */
	@Test
	public void insertDataFromTxtSingleTest() {

		String filePath = "g:/test.txt";
		new ReadTxtUtil().insertDataFromTxtSingle(filePath);
	}

	/**
	 * 
	 * @param filePath
	 */
	private void insertDataFromTxtSingle(String filePath) {
		long startTime = System.currentTimeMillis();

		insertDataFromTxt(filePath);

		long endTime = System.currentTimeMillis();
		logger.info("insertDataFromTxt 用时：{}ms-----------------------file:{}", (endTime - startTime), filePath);
	}

	/**
	 * 读取并插入数据
	 */
	private void insertDataFromTxt(String fileName) {
		// 读取文件
		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("file not exist!!!");
			return;
		} else {
			logger.info("to dealwith insertDataFromTxt file:" + fileName);
		}

		// 读取CSV文件
		List<String> readtTxtList = readTXT(fileName);
		
		// db insert
		 JdbcConnection.insertBatch(readtTxtList, dbTable);
	}

	/**
	 * 
	 * 
	 * 注：\n 回车(\u000a) 
		\t 水平制表符(\u0009) 
		\s 空格(\u0008) 
		\r 换行(\u000d)
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	private List<String> readTXT(String fileName){
		List<String> dataList = new ArrayList<String>(phoneListSize);
		BufferedReader reader = null;
		String lineStr = null;
		int line = 0;
		try {
			// 建立一个输入流对象reader  
			// 建立一个对象，它把文件内容转成计算机能读懂的语言  
	        reader = new BufferedReader(new InputStreamReader(
	        		new FileInputStream(fileName), Charset.forName("GBK"))); // ISO-8859-1  UTF-8  GBK
	        // 一次读入一行数据  
	        while ((lineStr = reader.readLine()) != null) {  
	        	line++;
	        	lineStr = lineStr.trim();
//	        	logger.info("---{}", lineStr);
	        	if (StringUtil.isBlank(lineStr)) {
	        		continue;
	        	}
	        	// 处理长行字符串
	        	String blankTag = "  ";
	        	if (lineStr.contains(blankTag)) {
	        		String[] splitStr = lineStr.split(blankTag);
	        		logger.info("the line tempStr split length is:{}", splitStr.length);
	        		for (String temp:splitStr) {
	        			String trimStr = temp.trim();
						if (trimStr.length() > 10) {
	        				dataList.add(trimStr);
	        			} else {
	        				if (StringUtil.isNotBlank(trimStr)) {
	        					logger.error("split after length less then 11, discard it:{}", trimStr);
	        				}
	        			}
	        		}
	        	} else if (lineStr.length() > 10) {
	        		dataList.add(lineStr);
	        	} else {
        			logger.error("original length less then 11, discard it:{}", lineStr);
	        	}
//	        	tempStr = StringUtil.replaceBlank(tempStr);
	        }  
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}  finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		logger.info("we have read data from txt file, fileName:{}, total line:{}, dataList size:{}", fileName, line, dataList.size());

        /* 写入Txt文件 */  
        // 相对路径，如果没有则要建立一个新的output.txt文件  
//        File writename = new File("g:/test.txt"); 
//        writename.createNewFile(); // 创建新文件  
//        BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
//        out.write("我会写入文件啦\r\n"); // \r\n即为换行  
//        out.flush(); // 把缓存区内容压入文件  
//        out.close(); // 最后记得关闭文件  

		
		return dataList;
	}

}
