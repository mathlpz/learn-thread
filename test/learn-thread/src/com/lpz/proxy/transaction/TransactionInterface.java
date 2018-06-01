package com.lpz.proxy.transaction;


/**
 * 2.要对service层的类进行代理，这些类必须至少实现一个接口，所以需要定义一个接口。
 * 
 * 定义接口代理后，自动走ProxyForTransactionService-invoke接口方法吗？
 * 
 * JDK动态代理
 * 
 * @author lpz
 *
 */
public interface TransactionInterface {
	
    @Transactionflag//表示该方法需要使用事务
    public void save();//定义save事务解决方法。
    
    //不加注解表示该方法不需要使用事务。
    public void query();
    
}