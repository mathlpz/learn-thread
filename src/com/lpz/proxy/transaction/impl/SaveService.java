package com.lpz.proxy.transaction.impl;

import com.lpz.proxy.transaction.TransactionInterface;


/**
 * 3.实现该接口的类看不出有事务的处理，但实际上已经对某些方法开启了事务。
 * 
 * @author lpz
 *
 */
public class SaveService implements TransactionInterface{
	
	
//    Dao1 dao1=new Dao1();
//    Dao2 dao2=new Dao2();
	
	
    @Override
    public void save()
    {
//        dao1.save();
//        dao2.save();
    }
    
    
    @Override
    public void query() {
//        dao1.query();
    }
}