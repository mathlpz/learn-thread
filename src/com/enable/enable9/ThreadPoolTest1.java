package com.enable.enable9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest1 {

	public void test1() {
		// 分配固定数量的线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		for (int j = 1; j <= 10; ++j) {
			final int taskNo = j;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " for task of " + taskNo);
				}

			});
		}
		threadPool.shutdown();
	}

	public void test2() {
		ExecutorService threadPool = null;

		// threadPool = Executors.newFixedThreadPool(3);// 固定数量的线程池
		// threadPool = Executors.newCachedThreadPool();// 缓存线程池
		threadPool = Executors.newSingleThreadExecutor();// 单个线程
		for (int i = 1; i <= 10; ++i) {
			final int taskNo = i;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (int j = 1; j <= 10; ++j) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(
								Thread.currentThread().getName() + " is loop of " + j + " for task of " + taskNo);
					}
					System.out.println("===========finish task" + taskNo);
				}
			});
		}

		System.out.println("all of 10 task has committed");
		threadPool.shutdown();
	}

	public void test3() {
		// 定时器,按照相对时间执行定时器
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
		threadPool.schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, 3, TimeUnit.SECONDS);
		threadPool.shutdown();
	}

	public void test4() {
		// 定时器，按照固定频率执行
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
		threadPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, 6, 2, TimeUnit.SECONDS);
		// threadPool.shutdown();
	}

	public static void main(String[] args) {
		ThreadPoolTest1 obj = new ThreadPoolTest1();
		// obj.test1();
		// obj.test2();
		// obj.test3();
		obj.test4();
	}
}
