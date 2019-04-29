package com.lpz.test;

import com.alibaba.fastjson.JSON;

public class MainTest {

    
    public static void main(String[] args) {
        
        System.out.println(JSON.toJSONString(null));
        
        String a = "aaa";
        String b = "aaa";
        String c = new String("aaa");
        String d = "ccc";
        String e = "a" + "b";
        
        System.out.println(a == b);
        System.out.println(a == c);
        
        e.intern();


        System.out.println("------");
        System.out.println("123456789".hashCode());

    }
    
}
