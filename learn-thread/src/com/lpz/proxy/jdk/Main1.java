package com.lpz.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


/**
 *  jdk
 *  Proxy
 *  InvocationHandler
 *  
 * @author lpz
 *
 */
public class Main1 {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        InvocationHandler invocationHandler = new MyInvocationHandler(userService);
        UserService userServiceProxy = (UserService)Proxy.newProxyInstance(
        		userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(), 
                invocationHandler);
        
        System.out.println(userServiceProxy.getName(1));
        System.out.println();
        System.out.println(userServiceProxy.getAge(1));
    }
}