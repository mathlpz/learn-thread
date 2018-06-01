package com.lpz.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示对List进行代理，只能对List进行代理。
 * 
 * 1.代理
 * 
 * 代理就使用一种方法在一个对象调用一个方法的时候拦截该方法的执行并改为执行另外一个动作。
 * 
 * 2.代理的核心类
 * 
 * （1）Proxy：在内存中生成该接口的一个实例。
 * 
 * （2）InvocationHandler：执行句柄，在执行代理类的方法的时候，该句柄会拦截所有代理类的方法。
 * 
 * 3.使用代理类的要求：被代理类必须至少实现一种接口。
 * 
 * 4.对List进行代理。
 * 
 * @author kdyzm
 *
 */
public class ProxyForListDemo {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final List<String> list = new ArrayList<String>();
		// 
		Object proxy = Proxy.newProxyInstance(
				ProxyForListDemo.class.getClassLoader(), 
				new Class[] { List.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println(method.getName() + "方法被调用！");
						return method.invoke(list, args);
					}
				}
			);
		// 
		@SuppressWarnings("unchecked")
		List<String> list2 = (List<String>) proxy;
		list2.add("小强！");
		System.out.println(list2.size());
		System.out.println(list2);
		
	}
}