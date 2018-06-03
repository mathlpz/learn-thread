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
	    String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中
	                                    // 的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//	    if (null == number || "" == number.trim()) {
//	        return false;
//	    } else {
	        //matches():字符串是否在给定的正则表达式匹配
	        return number.matches(num);
//	    }
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
	
}
