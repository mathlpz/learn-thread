package com.enable.enable11;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

	private Lock lock = new ReentrantLock();

	// 异步方法测试1
	public void aSynDemo0$1() {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + ",aSynDemo0$1 enter ");
		System.out.println(threadName + ",aSynDemo0$1 handle...");
		System.out.println(threadName + ",aSynDemo0$1 exit");
	}

	// 异步方法测试2
	public void aSynDemo0$2() {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + ",aSynDemo0$2 enter ");
		System.out.println(threadName + ",aSynDemo0$2 handle...");
		System.out.println(threadName + ",aSynDemo0$2 exit");
	}

	// lock测试
	public void synDemo1$1() {
		lock.lock();
		try {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo1$1 enter ");
			System.out.println(threadName + ",synDemo1$1 handle...");
			System.out.println(threadName + ",synDemo1$1 exit");

		} finally {
			lock.unlock();
		}
	}

	// lock测试
	public void synDemo1$2() {
		lock.lock();
		try {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo1$2 enter ");
			System.out.println(threadName + ",synDemo1$2 handle...");
			System.out.println(threadName + ",synDemo1$2 exit");
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final LockTest obj = new LockTest();

		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo1$1();
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo1$2();
			}
		};
		new Thread(runnable1).start();
		new Thread(runnable2).start();
	}
}
