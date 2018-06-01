package com.lpz.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 对ProxyForAllClassHasInterface类进行测试！
 * 
 * AOP的源码中用到了两种动态代理来实现拦截切入功能：jdk动态代理和cglib动态代理。两种方法同时存在，各有优劣。
 * jdk动态代理是由java内部的反射机制来实现的，cglib动态代理底层则是借助asm来实现的。
 * 总的来说，<反射机制在(生成类的过程中)比较高效>，而asm在<(生成类之后)的相关执行过程中>比较高效（可以通过将asm生成的类进行缓存，这样解决asm生成类过程低效问题）。
 * 还有一点必须注意：jdk动态代理的应用前提，必须是目标类基于统一的接口。如果没有上述前提，jdk动态代理不能应用。
 * 由此可以看出，jdk动态代理有一定的局限性，cglib这种第三方类库实现的动态代理应用更加广泛，且在效率上更有优势。。
 * 
 */
public class ProxyTest {
	
	/**
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
        List<String>list = new ArrayList<String>();
        list = (List<String>) ProxyForAllClassHasInterface.factory(list);
        list.add("你好，小强！");
        System.out.println(list);
        
        Map<String,String> map = new HashMap<String,String>();
        map = (Map<String, String>) ProxyForAllClassHasInterface.factory(map);
        map.put("12110501001", "小强");
        System.out.println(map);
    }

}