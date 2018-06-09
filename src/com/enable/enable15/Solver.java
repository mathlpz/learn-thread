package com.enable.enable15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//此代码来自jdk-api类CyclicBarrier
public class Solver {
	final int N;
	final float[][] data;
	final CyclicBarrier barrier;

	public Solver(float[][] matrix) {

		data = matrix;

		N = matrix.length;

		Runnable barrierAction = new Runnable() {
			@Override
			public void run() {
				mergeRows();

				System.out.println("全部完成了....");
			}

			private void mergeRows() {
				try {
					Thread.sleep((long) (Math.random() * 5000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		barrier = new CyclicBarrier(N, barrierAction);

		List<Thread> threads = new ArrayList<Thread>(N);
		for (int i = 0; i < N; i++) {
			Thread thread = new Thread(new Worker(i));
			threads.add(thread);
			thread.start();
		}

		// wait until done
		for (Thread thread : threads)
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	class Worker implements Runnable {
		int myRow;

		Worker(int row) {
			myRow = row;
		}

		@Override
		public void run() {
			// while (!done()) {
			processRow(myRow);
			try {
				barrier.await();
			} catch (InterruptedException ex) {
				return;
			} catch (BrokenBarrierException ex) {
				return;
			}
			// }
		}

		// private boolean done() {
		// try {
		// Thread.sleep((long) (Math.random() * 1000));
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// return true;
		// }

		private void processRow(int myRow2) {
			try {
				Thread.sleep((long) (Math.random() * 5000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(myRow2 + "处理好了...");
		}
	}

	public static void main(String[] args) {
		float[][] matrix = { { 1f, 2f, 3f }, { 5f, 6f, 7f } };
		// System.out.println(matrix.length);
		new Solver(matrix);
	}
}