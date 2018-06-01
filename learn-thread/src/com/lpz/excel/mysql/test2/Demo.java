package com.lpz.excel.mysql.test2;  
  
import java.sql.ResultSet;
import java.sql.SQLException;  
  
/**
 * 
 * @author lpz
 *
 */
public class Demo {  
  
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
  
    public static void main(String[] args) {  
    	
        sql = "select *from user"; //SQL语句  
        db1 = new DBHelper(sql); //创建DBHelper对象  
  
        try {  
            ret = db1.pst.executeQuery();//执行语句，得到结果集  
            while (ret.next()) {  
                String uid = ret.getString(1);  
                String ufname = ret.getString(2);  
                System.out.println(uid + "\t" + ufname );  
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
}