package com.lpz.DesignPattern.singleton;

/**
 * 4、静态内部类式。
 * <p>
 * 静态内部类式单例特点：线程安全（须synchronized做方法同步），可以延时加载，效率较高。
 *
 * @Author: lpz
 * @Date: 2019-04-25 09:11
 */
public class Singleton4 {

    // 静态内部类
    private static class InnerSingleton {
        private static final Singleton4 instance = new Singleton4();
    }

    private Singleton4() {
    }

    //公共静态方法获取唯一实例化对象，方法同步
    public static synchronized Singleton4 getInstance() {
        return InnerSingleton.instance;
    }
}
