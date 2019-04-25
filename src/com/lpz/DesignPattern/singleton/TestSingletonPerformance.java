package com.lpz.DesignPattern.singleton;

import java.util.concurrent.CountDownLatch;

/**
 * 测试单例模式各个实现方式的效率
 *
 * 饿汉式、枚举式总耗时最少，效率最高
 *
 * @Author: lpz
 * @Date: 2019-04-25 15:03
 */
public class TestSingletonPerformance {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        int threadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        for (; threadNum > 0; threadNum--) {
            new Thread(() -> {
                for(int i=0; i<1000000; i++){
//                    Singleton1 instance = Singleton1.getInstance();
//                    Singleton2 instance = Singleton2.getInstance();
//                    Singleton3 instance = Singleton3.getInstance();
//                    Singleton4 instance = Singleton4.getInstance();
                    Singleton5 instance = Singleton5.instance;
                }
                countDownLatch.countDown();
            }).start();
        }


        // Causes the current thread to wait until the latch has counted down to zero, unless the thread is interrupted.
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("饿汉式总耗时milliseconds：" + (end - start));
    }


}
