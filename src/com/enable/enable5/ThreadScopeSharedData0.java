package com.enable.enable5;

public class ThreadScopeSharedData0 {

	private static long data = 0L;

	public static void main(String[] args) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				data = System.currentTimeMillis();
				System.out.println(Thread.currentThread().getName() + " has put data:" + data);
				new A().get();
				new B().get();
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				data = System.currentTimeMillis();
				System.out.println(Thread.currentThread().getName() + " has put data:" + data);
				new A().get();
				new B().get();
			}
		}).start();

	}

	static class A {
		public void get() {
			System.out.println("A from:" + Thread.currentThread().getName() + " get data:" + data);
		}
	}

	static class B {
		public void get() {
			System.out.println("B from:" + Thread.currentThread().getName() + " get data:" + data);
		}
	}
}
