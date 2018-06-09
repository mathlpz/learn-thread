package com.enable.enable16.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Driver2 { // ...

	public static void main(String[] args) throws InterruptedException {

		final int N = 10;
		CountDownLatch doneSignal = new CountDownLatch(N);

		Executor e = Executors.newCachedThreadPool();

		for (int i = 0; i < N; ++i) // create and start threads
			e.execute(new WorkerRunnable(doneSignal, i));

		doneSignal.await(); // wait for all to finish
	}
}

class WorkerRunnable implements Runnable {
	private final CountDownLatch doneSignal;
	private final int i;

	WorkerRunnable(CountDownLatch doneSignal, int i) {
		this.doneSignal = doneSignal;
		this.i = i;
	}

	@Override
	public void run() {
		doWork(i);
		doneSignal.countDown();
	}

	void doWork(int i) {
	}
}