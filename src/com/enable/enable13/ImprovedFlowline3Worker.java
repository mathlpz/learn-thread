package com.enable.enable13;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImprovedFlowline3Worker {

	public static void main(String[] args) {

		final Business business = new Business();
		business.setNextStep(1);

		final int productSum = 40;

		// 子线程1
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					System.out.println("======1=====");
					business.step1(i);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					System.out.println("====2=======");
					business.step2(i);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					System.out.println("====3=======");
					business.step3(i);
				}
			}
		}).start();
	}
}

class Business {

	private Lock lock = new ReentrantLock();
	Condition condition1 = lock.newCondition();
	Condition condition2 = lock.newCondition();
	Condition condition3 = lock.newCondition();

	private volatile int nextStep = 1;

	public void setNextStep(int nextStep) {
		this.nextStep = nextStep;
	}

	public void step1(int i) {
		lock.lock();
		try {
			while (1 != nextStep) {
				try {
					condition1.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			for (int j = 1; j <= 10; ++j) {
				System.out.println("step1 Thread of sequence of " + j + "\tproduct of\t" + i);
			}
			nextStep = 2;
			condition2.signal();
		} finally {
			lock.unlock();
		}
	}

	public void step2(int i) {
		lock.lock();
		try {
			while (2 != nextStep) {
				try {
					condition2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			for (int j = 1; j <= 20; ++j) {
				System.out.println("step2 Thread of sequrence of " + j + "\tproduct of\t" + i);
			}
			nextStep = 3;
			condition3.signal();
		} finally {
			lock.unlock();
		}

	}

	public synchronized void step3(int i) {
		lock.lock();
		try {
			while (3 != nextStep) {
				try {
					condition3.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			for (int j = 1; j <= 30; ++j) {
				System.out.println("step3 Thread of sequrence of " + j + "\tproduct of\t" + i);
			}
			nextStep = 1;
			condition1.signal();
		} finally {
			lock.unlock();
		}
	}
}
