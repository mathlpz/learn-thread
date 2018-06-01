package com.lpz.proxy.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import com.lpz.proxy.ProxyForAllClassHasInterface;

/*
 * 对Service类进行代理，拦截特定的方法并进行修改，实现InvocationHandler接口是经典的做法。
 * 该类可以放在工具类中。
 * 
 * "反射+注解+动态代理"在事务中的应用service层
 * 
 * 1.解决的问题：事务使用OSIV模式并不高效，而且结构比较复杂，为了解决这个问题，可以使用反射+注解+动态代理的方式，这将称为最终的解决方式。

　　使用该方式的灵活性极高，事务的处理过程在service层解决，但是在serviece层的代码中又看不出来，实际上的事务处理在代理类中实现，service是否开启事务，仅仅只需要一句代码就可以解决。

　　2.核心类：代理service的ProxyForTransactionService类。
 */
public class ProxyForTransactionService implements InvocationHandler{
	
    private Object src;
    
    private ProxyForTransactionService(Object src) {
        this.src = src;
    }
    
    public static Object factory(Object src) {
        Object aim = Proxy.newProxyInstance(ProxyForAllClassHasInterface.class.getClassLoader(),
                src.getClass().getInterfaces(), 
                new ProxyForTransactionService(src));
        return aim;
    }
    
    /**
     * 重中之重！！
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Connection connection = null;
        Object returnValue = null;
        // 首先判断是否是经过注解的方法，如果是没有经过注解的方法则表明不需要开启事务！
        if(!method.isAnnotationPresent(Transactionflag.class)){
            System.out.println("不需要开启事务的方法：" + method.getName());
            return method.invoke(src, args);
        }
        
        // method、含有註解，開啟事務！
        System.out.println("需要开启事务的方法：" + method.getName());
        try{
            //获取连接
            connection = DataSourceUtils_c3p0.getConnection();
            //设置事务的开始
            connection.setAutoCommit(false);
            System.out.println("关闭事务自动提交，已经开启事务！");
            //调用方法
            returnValue = method.invoke(src, args);
            connection.commit();
            System.out.println("提交成功！结束事务！");
        } catch(Exception e){
            connection.rollback();
            System.out.println("回滚！结束事务！");
            throw e;//为什么能抛，因为Throwable是Exception的父类。
        } finally {
            connection.close();
            DataSourceUtils_c3p0.remove();
        }
        return returnValue;
    }
    
}