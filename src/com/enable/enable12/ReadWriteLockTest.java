package com.enable.enable12;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	// lock测试
	public void synDemo1$1() {
		rwl.readLock().lock();
		try {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo1$1 enter ");
			System.out.println(threadName + ",synDemo1$1 handle...");
			System.out.println(threadName + ",synDemo1$1 exit");

		} finally {
			rwl.readLock().unlock();
		}
	}

	// lock测试
	public void synDemo1$2() {
		rwl.writeLock().lock();
		try {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo1$2 enter ");
			System.out.println(threadName + ",synDemo1$2 handle...");
			System.out.println(threadName + ",synDemo1$2 exit");
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public static void main(String[] args) {

		final ReadWriteLockTest obj = new ReadWriteLockTest();

		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo1$2();
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo1$1();
			}
		};
		new Thread(runnable1).start();
		new Thread(runnable2).start();
	}
}
