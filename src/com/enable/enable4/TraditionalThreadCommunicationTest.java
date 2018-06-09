package com.enable.enable4;

public class TraditionalThreadCommunicationTest {

	private Object lock = new Object();

	public void test1() {
		System.out.println(Thread.currentThread() + ",enter test1");
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(Thread.currentThread() + ",exit test1");

	}

	public void test2() throws InterruptedException {
		Thread.sleep(4000);
		System.out.println(Thread.currentThread() + ",释放了锁");
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		final TraditionalThreadCommunicationTest obj = new TraditionalThreadCommunicationTest();

		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				obj.test1();
			}
		});

		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					obj.test2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		thread1.start();
		thread2.start();
	}
}
