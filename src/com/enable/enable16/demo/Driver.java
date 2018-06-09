package com.enable.enable16.demo;

import java.util.concurrent.CountDownLatch;

class Driver {

	public static void main(String[] args) throws InterruptedException {

		Driver driver = new Driver();

		final int N = 10;

		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);

		for (int i = 0; i < N; ++i) // create and start threads
			new Thread(new Worker(startSignal, doneSignal)).start();

		driver.doSomethingEls(); // don't let run yet
		startSignal.countDown(); // let all threads proceed
		doneSignal.await(); // wait for all to finish
		driver.doSomethingEls2();
	}

	private void doSomethingEls2() {
		System.out.println("driver receive all doWork has finish...");
		System.out.println("driver exit...");
	}

	private void doSomethingEls() throws InterruptedException {
		System.out.println("driver is init...");
		Thread.sleep((long) (Math.random() * 5000));
		System.out.println("driver init finish...");
	}
}

class Worker implements Runnable {
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;

	Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	@Override
	public void run() {
		try {
			startSignal.await();
			doWork();
			doneSignal.countDown();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} // return;
	}

	void doWork() throws InterruptedException {
		System.out.println("doWork start....");
		Thread.sleep((long) (Math.random() * 5000));
		System.out.println("doWork finish...");
	}
}