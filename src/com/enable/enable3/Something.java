package com.enable.enable3;

public class Something {

	public synchronized void isSyncA() {
		System.out.println("isSyncA enter ");
		System.out.println("isSyncA handle...");
		System.out.println("isSyncA exit");
	};

	public synchronized void isSyncB() {
		System.out.println("isSyncB enter ");
		System.out.println("isSyncB handle...");
		System.out.println("isSyncB exit");
	};

	public static synchronized void cSyncA() {
		System.out.println("cSyncA enter ");
		System.out.println("cSyncA handle...");
		System.out.println("cSyncA exit");
	};

	public static synchronized void cSyncB() {
		System.out.println("cSyncB enter ");
		System.out.println("cSyncB handle...");
		System.out.println("cSyncB exit");
	};

	public static void main(String[] args) {
		final Something obj = new Something();
		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				obj.cSyncA();
			}
		};
		Runnable runnable2 = new Runnable() {
			@Override
			public void run() {
				obj.isSyncA();
			}
		};
		new Thread(runnable1).start();
		new Thread(runnable2).start();
	}
}
