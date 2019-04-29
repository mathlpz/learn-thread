package com.lpz.test.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: lpz
 * @Date: 2019-04-29 19:34
 */
public class TwoThread2 {

    final static String str = "abcdefghijk";
    volatile AtomicInteger flag = new AtomicInteger(0);
    final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            list.add(str.substring(i, i + 1));
        }
        System.out.println(list + " size: " + list.size());

        TwoThread2 twoThread = new TwoThread2();
        for (int i = 0, num = list.size(); i <= num / 2; i++) {
            // 偶数线程
            new Thread(() -> {
                twoThread.printNull(list);
            }, "null").start();
            // 基数线程
            new Thread(() -> {
                twoThread.printFull(list);
            }, "full").start();
        }
    }


    public void printNull(List<String> list) {
        try {
            while (flag.get() == list.size()) {
                return;
            }
            queue.put(1);
            System.out.println(Thread.currentThread().getName() + " --- " + flag + " --- " + list.get(flag.get()));
            flag.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void printFull(List<String> list) {
        try {
            while (flag.get() == list.size()) {
                return;
            }
            queue.take();
            System.out.println(Thread.currentThread().getName() + " --- " + flag + " --- " + list.get(flag.get()));
            flag.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
