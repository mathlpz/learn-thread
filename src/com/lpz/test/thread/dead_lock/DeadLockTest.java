package com.lpz.test.thread.dead_lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lpz
 * @Date: 2019-04-19 14:22
 */
public class DeadLockTest {


    public static void main(String[] args) throws InterruptedException {
        // 抢笔
        Thread t1 = new Thread(new PenPaperThread(true));
        // 抢纸
        Thread t2 = new Thread(new PenPaperThread(false));

//        // 同时启动
//        t1.start();
//        t2.start();


        LinkedBlockingQueue<Runnable> linkedQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 10,
                1000, TimeUnit.SECONDS, queue);
//        threadPool

        orderPrintNum2(3);
    }


    /**
     * 通过N个线程顺序循环打印从0至100，如给定N=3则输出:
     thread0: 0
     thread1: 1
     thread2: 2
     thread0: 3
     thread1: 4
     */
    static int result = 0;
    public static void orderPrintNum2(final int n) throws InterruptedException{
        // N 个线程
        Thread [] threads = new Thread[n];
        // N 个Semaphore
        final Semaphore[] syncSemaphore = new Semaphore[n];
        for (int i = 0; i < n; i++) {
            //非公平信号量，计数为1
            syncSemaphore[i] = new Semaphore(1);
            if (i != n-1){
                //获取一个许可前线程将一直阻塞
                syncSemaphore[i].acquire();
            }
        }
        for (int i = 0; i < n; i++) {
            final Semaphore lastSemphore = i == 0 ? syncSemaphore[n - 1] : syncSemaphore[i - 1];
            final Semaphore curSemphore = syncSemaphore[i];
            final int index = i;
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            lastSemphore.acquire();
                            System.out.println("thread" + index + ": " + result++);
                            if (result > 100){
                                System.exit(0);
                            }
                            curSemphore.release();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            threads[i].start();
        }
    }

//    static int result = 0;
    public static void orderPrintNum3(final int n) {
        final Object LOCK = new Object();
        final int [] flag = new int[1];
        for (int i = 0;i < n; i++){
            final int index = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (result <= 100) {
                            synchronized (LOCK){
                                if(flag[0] == index){
                                    System.out.println("thread" + index + ": " + result++);
                                    flag[0] = index == n - 1 ? 0 : index + 1;
                                    // 唤醒其他wait线程
                                    LOCK.notifyAll();
                                }else {
                                    LOCK.wait();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }



}
