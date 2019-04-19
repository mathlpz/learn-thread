package com.lpz.test.thread.dead_lock;

/**
 * @Author: lpz
 * @Date: 2019-04-19 14:51
 */
public class PenPaperThread implements Runnable {


    // 先抢哪一个东西的标记
    boolean flag;

    public PenPaperThread(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {

        if (flag) {
            synchronized (LockObject.PEN) {
                System.out.println("0-pen, " + Thread.currentThread().getName());
                synchronized (LockObject.PAPER) {
                    System.out.println("0-paper, " + Thread.currentThread().getName());
                }
            }

        } else {
            synchronized (LockObject.PAPER){
                System.out.println("1-paper, " + Thread.currentThread().getName());
                synchronized (LockObject.PEN){
                    System.out.println("1-pen, " + Thread.currentThread().getName());
                }
            }
        }

    }

}
