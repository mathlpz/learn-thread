package com.lpz.test.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有字符串123456789这样的, 有两个线程同时执行,实现一下一次循环遍历出123456789 例如：t1线程结果是1 t2线程结果是2 再是t1 = 3这样的.
 *
 * @Author: lpz
 * @Date: 2019-04-25 16:48
 */
public class TwoThread {

    final static String str = "abcdefghijk";
    volatile AtomicInteger flag = new AtomicInteger(0);

    ReentrantLock lock = new ReentrantLock();
    Condition oddCondi = lock.newCondition();
    Condition evenCondi = lock.newCondition();


    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            list.add(str.substring(i, i + 1));
        }
        System.out.println(list + " size: " + list.size());

        TwoThread twoThread = new TwoThread();
        for (int i = 0, num = list.size(); i <= num/2; i++) {
            // 偶数线程
            new Thread(() -> {
                twoThread.printEvenNum(list);
            }, "even").start();
            // 基数线程
            new Thread(() -> {
                twoThread.printOddNum(list);
            }, "odd").start();
        }


    }


    /**
     * 打印奇数odd
     */
    public void printOddNum(List<String> list) {
        try {
            lock.lockInterruptibly();
            while (flag.get() == list.size()) {
                return;
            }
            // 若偶数，奇数线程等待
            while (flag.get() % 2 == 0) {
                oddCondi.await();
            }
            //奇数来了，doing odd
            // 偶尔报异常：Exception in thread "odd" java.lang.IndexOutOfBoundsException: Index: 11, Size: 11
            // 但是上面有flag的return判断啊。。。求解
            System.out.println(Thread.currentThread().getName() + " --- " + flag + " --- " + list.get(flag.get()));
            flag.incrementAndGet();
            // 唤起偶数线程
            evenCondi.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }


    /**
     * 打印偶数even
     */
    public void printEvenNum(List<String> list) {
        try {
            lock.lockInterruptibly();
            // 使用while代替if，防止虚假唤醒？
            while (flag.get() == list.size()) {
                return;
            }
            // 奇数情况下，偶数等待
            while (flag.get() % 2 == 1) {
                evenCondi.await();
            }
            // 偶数来了，doing even
            System.out.println(Thread.currentThread().getName() + " --- " + flag + " --- " + list.get(flag.get()));
            flag.getAndIncrement();
            // 唤起奇数线程
            oddCondi.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
