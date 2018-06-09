package com.enable.enable8;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
	public static void main(String[] args) {

		AtomicInteger atomicInter = new AtomicInteger(10);
		atomicInter.addAndGet(3);
		atomicInter.compareAndSet(13, 14);
	}
}
