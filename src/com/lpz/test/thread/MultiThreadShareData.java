package com.lpz.test.thread;

public class MultiThreadShareData {
	
//	static ShareData2 data = new ShareData2(0, 3);
	
	public static void main(String[] args) {
//		ShareData shareData = new ShareData();
//		for (int i=1; i<200; i++) {
//			new Thread(shareData).start();
//		}
		
		// 方法1
		ShareData2 data = new ShareData2(0, 3);
		new Thread(new ShareRunnable1(data)).start();
		new Thread(new ShareRunnable2(data)).start();
		
		// 方法2
//		for (int i=0; i<2; i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					data.increment();
//				}
//			}).start();
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					data.decrement();
//				}
//			}).start();
//		}
	}
	

}

/**
 * 如果线程代码相同，可以使用同一个runnable对象，runnable对象中有那个共享数据，如买票系统
 * @author lpz
 *
 */
class ShareData implements Runnable{
	private int count = 1000;
	@Override
	public /*synchronized*/ void run() {
		while (count > 0) {
			count--;
			System.out.println(count);
		}
	}
}

/**
 * 如果每个线程执行的代码不同，需要使用不同的runnable对象，有一下两种方式实现runnable对象之间的数据共享
 * @author lpz
 *
 */
class ShareRunnable1 implements Runnable{
	private ShareData2 data;
	public ShareRunnable1(ShareData2 data) {
		this.data = data;
	}
	@Override
	public void run() {
		for (int k=0; k<data.getNum(); k++) {
			data.increment();
		}
	}
}
class ShareRunnable2 implements Runnable{
	private ShareData2 data;
	public ShareRunnable2(ShareData2 data) {
		this.data = data;
	}
	@Override
	public void run() {
		for (int k=0; k<data.getNum(); k++) {
			data.decrement();
		}
	}
}
class ShareData2 {
	private int count = 0;
	private int num = 0;
	public ShareData2(int count, int num) {
		this.count = count;
		this.num = num;
	}
	public synchronized void decrement(){
		count--;
		System.out.println(count);
	}
	public synchronized void increment(){
		count++;
		System.out.println(count);
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}

