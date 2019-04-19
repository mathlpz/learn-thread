package com.lpz.test.thread.dead_lock;

/**
 * @Author: lpz
 * @Date: 2019-04-19 14:22
 */
public class DeadLockTest {


    public static void main(String[] args) {
        // 抢笔
        Thread t1 = new Thread(new PenPaperThread(true));
        // 抢纸
        Thread t2 = new Thread(new PenPaperThread(false));

        // 同时启动
        t1.start();
        t2.start();

    }


}
