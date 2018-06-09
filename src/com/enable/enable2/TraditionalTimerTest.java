package com.enable.enable2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TraditionalTimerTest {

	public void test1() {
		Timer timer = new Timer();
		System.out.println("111");
	}

	public void afterAllMethod() {
		new Thread() {
			public void run() {
				try {
					int i = 0;
					while (true) {
						Thread.sleep(1000);
						System.out.println(i++);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	// 间隔10秒后爆炸
	public void method1() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, 6000);
	}

	// 2秒后爆炸第一次，然后每隔3秒爆炸
	public void method2() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, 2000, 3000);
	}

	// 在2017-05-11 23:08:05定时爆炸
	public void method3() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse("2017-05-12 23:08:05");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, date);
	}

	// 在2017-05-11 23:08:05定时爆炸，然后每隔2秒爆炸
	public void method4() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = sdf.parse("2017-05-11 23:11:05");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("bombing");
			}
		}, date, 2000);
	}

	private static int count = 0;

	// 交替间隔时间连环爆炸
	// 功能：2秒钟后爆炸一次，然后4秒钟后爆炸，接着2秒钟后爆炸，然后继续4秒钟后爆炸，接着2秒钟后爆炸，然后继续4秒钟后爆炸,如此循环下去
	public void method5() {
		new Timer().schedule(new MyTimerTask(), 2000);
	}

	public static void main(String[] args) {
		TraditionalTimerTest obj = new TraditionalTimerTest();
		// obj.test1();
		// obj.method1();
		// obj.method2();
		// obj.method3();
		// obj.method4();
		obj.method5();
		obj.afterAllMethod();

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			System.out.println("bombing");
			count = (count + 1) % 2;
			new Timer().schedule(new MyTimerTask(), 2000 * (1 + count));
			this.cancel();// 后浪消失前把前浪推出去,后浪不消失，会有什么问题？
		}
	}

}
