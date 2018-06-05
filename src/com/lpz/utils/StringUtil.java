package com.lpz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils{
	
	
	/**
	 * 校验手机号格式
	 *
	 * @param number
	 * @return
	 */
	public static boolean isMobileNum(String number) {
	    /*
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	    联通：130、131、132、152、155、156、185、186
	    电信：133、153、177、178、180、189、（1349卫通）
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
	    */
	    String num = "[1][345678]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中
	                                    // 的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        //matches():字符串是否在给定的正则表达式匹配
        return number.matches(num);
	}
	
	/**
	 * 空格、回车、换行符、制表符
	 * \n 回车(\u000a) 
		\t 水平制表符(\u0009) 
		\s 空格(\u0008) 
		\r 换行(\u000d)
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			return m.replaceAll("").trim();
		}
		return null;
	}
	
	private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
	
	/**
	 * // 去除中文
	 * @param str
	 * @return
	 */
	public static String removeChineseStr(String str) {
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(str);
        return mat.replaceAll("").trim();
    }
	
	
		
	//方法一：用JAVA自带的函数
	public static boolean isNumeric1(String str){
	   for (int i = str.length();--i>=0;){  
	       if (!Character.isDigit(str.charAt(i))){
	           return false;
	       }
	   }
	   return true;
	}

	 /*方法二：推荐，速度最快
	  * 判断是否为整数 
	  * @param str 传入的字符串 
	  * @return 是整数返回true,否则返回false 
	  */
	  public static boolean isInteger(String str) {  
	        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	        return pattern.matcher(str).matches();  
	  }
	  
	  //方法三：
	  public static boolean isNumeric3(String str){
	      Pattern pattern = Pattern.compile("[0-9]*");
	      return pattern.matcher(str).matches();   
	  }
	  
	  //方法四：
	  public final static boolean isNumeric4(String s) {
	      if (s != null && !"".equals(s.trim()))
	          return s.matches("^[0-9]*$");
	      else
	          return false;
	  }
	  
	  //方法五：用ascii码 
	  public static boolean isNumeric5(String str){
	      for(int i=str.length();--i>=0;){
	          int chr=str.charAt(i);
	          if(chr<48 || chr>57)
	              return false;
	      }
	     return true;
	  }
	  
}
