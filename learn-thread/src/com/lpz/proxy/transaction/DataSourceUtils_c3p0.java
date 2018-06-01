package com.lpz.proxy.transaction;



import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

//import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * 数据库连接池工具类。
 */
public class DataSourceUtils_c3p0 {
    private static ThreadLocal<Connection>tl=new ThreadLocal<Connection>();
    private static DataSource ds = null;
    static{
//        ds = new ComboPooledDataSource("namedconfig");
    }
    public static Connection getConnection(){
        Connection conn=tl.get();
        if(conn==null)
        {
            try {
                conn = ds.getConnection();
                tl.set(conn);//这句代码十分重要，千万不能忘（将当前线程和请求的Connection对象绑定）
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
    public static DataSource getDataSource(){
        return ds;
    }
    public static void remove(){
        tl.remove();//这句代码也十分重要，千万不能忘掉，将会在TransactionFilter中调用
    }
}    
