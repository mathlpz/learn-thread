package com.lpz.proxy.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * "反射+注解+动态代理"，在事务中的应用service层
 * 
 * 　4.自定义一个注解，对接口上的某些方法进行注解。
 * @author lpz
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface Transactionflag {
    
	
}