package com.lpz.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
 * 演示对所有拥有接口的类进行代理,并且该类实现了InvocationHandler接口。
 * 这种方式是推荐的代理方式。
 * 
 * 1.使用一个类封装代理的过程。该类实现了InvocationHandler接口。
 * 
 */
public class ProxyForAllClassHasInterface implements InvocationHandler {
	
	private Object src;

	private ProxyForAllClassHasInterface(Object src) {
		this.src = src;
	}

	public static Object factory(Object src) {
		Object aim = Proxy.newProxyInstance(
				ProxyForAllClassHasInterface.class.getClassLoader(),
				src.getClass().getInterfaces(), 
				new ProxyForAllClassHasInterface(src)
			);
		return aim;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(method.getName() + "方法被调用, ProxyForAllClassHasInterface！");
		// 调用src的方法。
		return method.invoke(src, args);
	}
	
	
}