package com.lpz.test.interview;

/**
 * 通过两个线程,一个调用aaa方法一个调用bbb方法,问线程调用了bbb方法后 aaa方法还能同时进入吗.
 *
 * @Author: lpz
 * @Date: 2019-04-25 16:15
 */
public class StaticThread {


    synchronized void aaa() {
        System.out.println("......aaaaaa!!!......");
    }

    synchronized static void bbb() {
        try {
            System.out.println("bbbbbb in...");
            Thread.sleep(5000);
            System.out.println("bbbbbb out...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 经过测试: 一个调用bbb(static)方法,一个调用aaa方法, 问:线程调用了bbb方法后,aaa方法还能同时进入！！！
     * 注：如果两个方法都是static，则不能同时进入!
     *
     * @param args
     */
    public static void main(String[] args) {

        StaticThread twoThread = new StaticThread();
        new Thread(() -> {
            StaticThread.bbb();
        }).start();
        new Thread(() -> {
            twoThread.aaa();
        }).start();

        // 计时
        new Thread(() -> {
            int time = 0;
            while (time < 10) {
                System.out.println(time);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;
            }
        }).start();
    }


}
