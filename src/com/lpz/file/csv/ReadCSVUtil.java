package com.lpz.file.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.lpz.utils.StringUtil;

/**
 * 利用javacsv2.0读写csv文件工具类
 * 
 * @author lpz
 *
 */
public class ReadCSVUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(ReadCSVUtil.class);
	
	public static String dbTable = "user_fang";
	
	// file name
	public static String directoryPath = "g:/41csv/qita";
//	public static String filePath = "g:/41csv/fang/Campaign-fang-1.csv";
	
	public static char separator = ',';

	public static void main(String[] args) {
		logger.info("123!!!");
		
		// 读取文件
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			logger.error("directoryPath not exist!!!");
            return ;
        }
		File[] files = baseFile.listFiles();
		logger.info("~~directoryPath:{}, listFiles size:{}", directoryPath, files.length);
		
		// 创建生成导入csv文件
		// createCSV(filePath);

	}


	
	/**
	 * 读取并插入数据
	 */
	@Test
	public void insertDataFromCsvTest() {
		ReadCSVUtil csvUtil = new ReadCSVUtil();
		// 读取文件
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			logger.error("directoryPath not exist!!!");
            return ;
        }
		File[] files = baseFile.listFiles();
		logger.info("~~directoryPath:{}, listFiles size:{}", directoryPath, files.length);
		
		for (File fileName:files) {
			
			String filePath = fileName.getAbsolutePath();
			
			long startTime = System.currentTimeMillis();
			
			csvUtil.insertDataFromCsv(filePath);
			
			long endTime = System.currentTimeMillis();
			logger.info("insertDataFromCsv 用时：{}ms-----------------------file:{}", (endTime - startTime), filePath);
		}
	}
	
	
	/**
	 * 读取并插入数据
	 */
	private void insertDataFromCsv(String fileName){
		// 读取文件
		File file = new File(fileName);
		if (!file.exists()) {
			logger.error("file not exist!!!");
			return;
		} else {
			logger.info("to dealwith insertDataFromCsv file:" + fileName);
		}

		// 读取CSV文件
		List<String> readCSVList = readCSV(fileName);
		logger.info("we have read data from CSV file, List size:" + readCSVList.size());

		// db insert
//		JdbcConnection.insertBatch(readCSVList, dbTable);
	}

	/**
	 * 读取CSV文件
	 * 
	 * @param filePath:全路径名
	 */
	public static List<String> readCSV(String filePath) {
		CsvReader reader = null;
		List<String> dataList = new ArrayList<String>();
		try {
			// 如果生产文件乱码，windows下用gbk，linux用UTF-8
			reader = new CsvReader(filePath, separator, Charset.forName("GBK"));
			/*// 读取表头。如果获取表头信息，则后续读取内容不再读取第一行数据；否则从第一行开始读取；
			reader.readHeaders();
			// 获取标题
			String[] headArray = reader.getHeaders();
			System.out.println("getHeaders:" + headArray[0] + separator + headArray[1] + separator + headArray[2]);*/

			// 逐条读取记录，直至读完
			while (reader.readRecord()) {
				// 读一整行，想获取第一列数据必须先读取第一行；
				@SuppressWarnings("unused")
				String rawRecord = reader.getRawRecord();
//				System.out.println(rawRecord);
				// 读这行的第一列，可用列名（如学号）或序号（从0开始）
				String phone = reader.get(0).trim();
				if (StringUtil.isNotBlank(phone) && phone.length() > 10) {
					// 非空且>=11
					dataList.add(phone);
				} else {
					logger.error("date is not phone:" + phone);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				reader.close();
			}
		}

		return dataList;
	}
	
	
	
	/**
	 * 生成CSV文件
	 * 
	 * @param filePath:全路径名
	 */
	public static boolean createCSV(String filePath) {
		// 测试导出
		List<String[]> dataList = new ArrayList<String[]>();
		// 添加标题
		dataList.add(new String[] { "学号", "姓名", "分数" });
		for (int i = 0; i < 10; i++) {
			dataList.add(new String[] { "2010000" + i, "张三" + i, "8" + i });
		}

		return createCSV(dataList, filePath);
	}

	/**
	 * 生成CSV文件
	 * 
	 * @param dataList
	 * @param filePath
	 * @return
	 */
	public static boolean createCSV(List<String[]> dataList, String filePath) {
		//
		boolean isSuccess = false;
		CsvWriter writer = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath, true);
			// 如果生产文件乱码，windows下用gbk，linux用UTF-8
			writer = new CsvWriter(out, separator, Charset.forName("GBK"));
			for (String[] strs : dataList) {
				writer.writeRecord(strs);
			}
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSuccess;
	}

}