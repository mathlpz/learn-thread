package com.enable.enable1;

public class TraditionalThread {

	public void method1() {
		// 方式1：继承Thread，覆盖run()方法
		class myThread extends Thread {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("3:myThread--");
				}
			}
		}
		Thread thread3 = new myThread();
		thread3.start();
	}

	public void method2() {
		// 方式1：使用Thread的匿名实现类
		Thread thread1 = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					System.out.println("2:" + this.getName());
					// System.out.println("3:" + getName());
				}
			}
		};
		thread1.start();
	}

	public void method3() {
		// 方式2：使用Runnable接口
		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("2:" + Thread.currentThread().getName());
				}
			}
		});
		thread2.start();
	}

	/* volatile */ boolean isStop = false;

	// 此方法为�?么会出现死循�?
	public void method4() {

		Thread thread1 = new Thread() {
			@Override
			public void run() {
				System.out.println("ready into loop...");
				while (!isStop) {
					// int a = 1;
					// a++;
				}
				System.out.println("===111===" + System.currentTimeMillis());
			}
		};
		thread1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName());
		isStop = true;
		System.out.println("===333===" + System.currentTimeMillis());
	}

	// 题目：以下出现运行结果是�?么？runnable还是thread
	public void method5() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("runnable:" + Thread.currentThread().getName());
				}
			}
		}) {
			@Override
			public void run() {
				// private Runnable java.lang.Thread.target
				// 如果父类target定义为public结果是什么？
				// 子类构�?�器会自动调用父类构造器，但是调用子类重写的方法时，会调用父类原始的被重写的方法�?
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread:" + Thread.currentThread().getName());
				}
			};
		}.start();
	}

	public void method6() {
		Thread thread1 = new Thread() {
			@Override
			public void run() /* throws InterruptedException */ {
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }

				// Thread.sleep(1000);
			}
		};
		thread1.start();
	}

	public void method7() {
		Thread thread1 = new Thread() {
			@Override
			public void run() {
				System.out.println(222);
			}
		};

		thread1.start();

		System.out.println(111);
	}

	public static void main(String[] args) {
		TraditionalThread obj = new TraditionalThread();
		// obj.method1();
		// obj.method2();
		// obj.method3();
		// obj.method4();
		// obj.method5();
		// obj.method6();
		obj.method7();
	}

}
