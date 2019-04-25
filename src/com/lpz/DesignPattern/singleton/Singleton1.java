package com.lpz.DesignPattern.singleton;

/**
 * 单例模式：类只能有一个实例。
 * <p>
 * 　　类的特点：1、私有构造器；2、内部构造实例对象；3、对外提供获取唯一实例的public方法。
 * <p>
 * 　　常见的单例模式实现有五种形式：
 * <p>
 * 　　　　1、饿汉式。
 * <p>
 * 　　　　2、懒汉式。
 * <p>
 * 　　　　3、双重检查锁式。
 * <p>
 * 　　　　4、静态内部类式。
 * <p>
 * 　　　　5、枚举式。
 *
 * @Author: lpz
 * @Date: 2019-04-25 08:57
 */
public class Singleton1 {

    // 饿汉式单例特点：线程安全，不能延时加载，效率较高。
    // 内部构建唯一实例
    private static Singleton1 instance = new Singleton1();

    //私有化构造器
    private Singleton1() {
    }

    //公共静态方法获取唯一实例化对象
    public static Singleton1 getInstance() {
        return instance;
    }
}
