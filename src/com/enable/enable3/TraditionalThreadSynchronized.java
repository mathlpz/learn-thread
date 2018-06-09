package com.enable.enable3;

public class TraditionalThreadSynchronized {

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

	// 同步方法测试
	public synchronized void synDemo1$1() {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + ",synDemo1$1 enter ");
		System.out.println(threadName + ",synDemo1$1 handle...");
		System.out.println(threadName + ",synDemo1$1 exit");
	}

	// 同步块
	public void synDemo1$2() {
		synchronized (this) {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo1$2 enter ");
			System.out.println(threadName + ",synDemo1$2 handle...");
			System.out.println(threadName + ",synDemo1$2 exit");
		}
	}

	// 静态同步方法测试
	public synchronized static void synDemo2$1() {
		String threadName = Thread.currentThread().getName();
		System.out.println(threadName + ",synDemo2$1 enter ");
		System.out.println(threadName + ",synDemo2$1 handle...");
		System.out.println(threadName + ",synDemo2$1 exit");
	}

	// 静态同步块测试
	public static void synDemo2$2() {
		synchronized (TraditionalThreadSynchronized.class) {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo2$2 enter ");
			System.out.println(threadName + ",synDemo2$2 handle...");
			System.out.println(threadName + ",synDemo2$2 exit");
		}
	}

	// 局部变量加锁
	public void synDemo3$1(String mutex) {
		synchronized (mutex) {
			String threadName = Thread.currentThread().getName();
			System.out.println(threadName + ",synDemo2$2 enter ");
			System.out.println(threadName + ",synDemo2$2 handle...");
			System.out.println(threadName + ",synDemo2$2 exit");
		}
	}

	public static void main(String[] args) {
		final TraditionalThreadSynchronized obj = new TraditionalThreadSynchronized();
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo3$1("张三");
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				obj.synDemo3$1("张三");
			}
		};
		new Thread(runnable1).start();
		new Thread(runnable2).start();
	}
}
