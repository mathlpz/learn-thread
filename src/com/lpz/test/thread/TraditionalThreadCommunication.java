package com.lpz.test.thread;

public class TraditionalThreadCommunication {

	/**
	 * 两个线程，交替打印数据；子线程打印10次，主线程打印20次，交替执行20次
	 * 使用wait(), notify()在两个线程间通信
	 * @param args
	 */
	public static void main(String[] args) {
		
		final businessDeal business = new businessDeal();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i=1; i<21; i++) {
					business.sub(i);
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i=1; i<21; i++) {
					business.main(i);
				}
			}
		}).start();
		
	}
	
}

/**
 * 封装业务类
 * @author lpz
 *
 */
class businessDeal {
	
	private boolean isSub = true;
	
	/**
	 * 子线程
	 * @param i
	 */
	public synchronized void sub(int i){
		// 避免虚假唤醒
		// 不是子线程执行，则子线程挂起等待，放弃资源锁
		while (!isSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 是子线程执行，获取锁并执行
		for (int j=1; j<11; j++) {
			System.out.println("sub thread print out:" + j +",sequence is:" + i);
		}
		// 执行结束，更改标识，唤醒等待线程-主线程
		isSub = false;
		this.notify();
	}
	
	/**
	 * 主线程
	 * @param i
	 */
	public synchronized void main(int i){
		while (isSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int j=1; j<21; j++) {
			System.out.println("main thread print out:" + j +",sequence is:" + i);
		}
		isSub = true;
		this.notify();
	}
	
}
