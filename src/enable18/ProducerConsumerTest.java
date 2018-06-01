package enable18;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {

	private final BlockingQueue<Integer> queue;

	Producer(BlockingQueue<Integer> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep((long) (Math.random() * 50));
				queue.put(produce());
			}
		} catch (InterruptedException ex) {
		}
	}

	Integer produce() {
		int ret = (int) (Math.random() * 5000);
		System.out.println("==============生产了" + ret);
		return ret;
	}
}

class Consumer implements Runnable {

	private final BlockingQueue<Integer> queue;

	Consumer(BlockingQueue<Integer> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep((long) (Math.random() * 100));
				consume(queue.take());
			}
		} catch (InterruptedException ex) {
		}
	}

	void consume(Integer x) {
		System.out.println("消费了" + x.intValue());
	}
}

public class ProducerConsumerTest {
	public static void main(String[] args) {
//		BlockingQueue<Integer> q = new LinkedBlockingQueue<Integer>();
		BlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(5);
		Producer p = new Producer(q);
		Consumer c1 = new Consumer(q);
		Consumer c2 = new Consumer(q);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}