package com.lpz.DesignPattern.singleton;

import java.io.Serializable;

/**
 * 2、懒汉式。
 * instance是static修饰
 *
 * @Author: lpz
 * @Date: 2019-04-25 09:03
 */
public class Singleton2 implements Serializable{

    //声明实例对象
    private static Singleton2 instance = null;

    private Singleton2() {
    }

    //公共静态方法获取唯一实例化对象，“方法同步”
    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

}
