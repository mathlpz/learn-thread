package com.lpz.DesignPattern.singleton;

/**
 * 3、双重检查锁式。
 * <p>
 * 结合了饿汉式和懒汉式的优点，但由于JVM底层内部模型原因，偶尔会出问题，所以不建议使用，本文不赘语。
 *
 * @Author: lpz
 * @Date: 2019-04-25 09:08
 */
public class Singleton3 {

    private static volatile Singleton3 instance = null;

    private Singleton3() {
    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }

        }
        return instance;
    }
}
