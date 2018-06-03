package com.lpz.test.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

public class TestSuspend {

	public static Object u = new Object();
	
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");
	
	public static void main(String[] args) throws InterruptedException {
		Integer i = 0;
		
		t1.start();
		Thread.sleep(100);
		t2.start();
		t1.resume();
		t2.resume();
//		t1.join();
//		t2.join();
		
		LockSupport.unpark(t1);
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		lock.unlock();
//		ConcurrentLinkedQueue<E>
//		LinkedBlockingDeque<E>
//		ConcurrentHashMap<K, V>
//	    ArrayBlockingQueue<E>
//		Executors
//		AbstractExecutorService
		
//		new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
//		return new ForkJoinPool(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
		
//		return new FinalizableDelegatedExecutorService();
		
		StampedLock locks = new StampedLock();
		long stamp = locks.writeLock();
		
		if (locks.validate(stamp)) {
			
		}
		
		locks.unlockWrite(stamp);
		locks.unlockRead(stamp);
		
		AtomicInteger inte = new AtomicInteger(1);
		inte.incrementAndGet();
		
		AtomicStampedReference<Integer> asf = new AtomicStampedReference<Integer>(10, 0);
		
	}
	
	
	/**
	 * 
	 * @author lpz
	 *
	 */
	public static class ChangeObjectThread extends Thread {
		
		public ChangeObjectThread(String name) {
			super.setName(name);
		}

		@Override
		public void run() {
			synchronized (u) {
				System.out.println("in " + getName() + ", below is suspend");
				Thread.currentThread().suspend();
				System.out.println("in " + getName() + ", allready suspend");
			}
		}
		
	}
	
	
}
