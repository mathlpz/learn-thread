package com.enable.enable14;

import java.util.concurrent.Semaphore;

//此代码来自jdk1.8的Semaphore类的案例,通过Semaphore实现了对资源池的控制
public class Pool {

	private static final int MAX_AVAILABLE = 100;// 资源池和信号的许可数量相同的
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

	// Not a particularly efficient data structure; just for demo
	protected Object[] items = /* ...whatever kindsof items being managed */ null;
	protected boolean[] used = new boolean[MAX_AVAILABLE];

	public Object getItem() throws InterruptedException {
		available.acquire();
		return getNextAvailableItem();
	}

	public void putItem(Object x) {
		if (markAsUnused(x))
			available.release();
	}

	/* protected */ private synchronized Object getNextAvailableItem() {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		return null; // not reached
	}

	/* protected */ private synchronized boolean markAsUnused(Object item) {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}
}