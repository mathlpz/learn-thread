package com.lpz.DesignPattern.singleton;

/**
 * 5、枚举式。
 * <p>
 * 枚举式单例特点：枚举是天然的单例，线程安全，不可延时加载，效率较高
 *
 * @Author: lpz
 * @Date: 2019-04-25 09:17
 */
public enum Singleton5 {

    //枚举元素，本身就是单例模式
    instance;

    //实现自己的操作
    Singleton5() {
    }
}
