package enable13;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExerciseDemo {

	public void test1() {
		String abc = "abc";
		try {
			abc.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void test2() {
		synchronized (this) {
			System.out.println("哈哈哈");
		}
	}

	public void test3() {
		Lock lock = new ReentrantLock();
		lock.lock();
		System.out.println("111");
		System.out.println("222");
		System.out.println("333");
		lock.unlock();
	}

	public void test4() {
		Lock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
//		lock.lock();
		try {
			condition.signal();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("111");
		System.out.println("222");
		System.out.println("333");
//		lock.unlock();
	}

	public static void main(String[] args) {
		ExerciseDemo obj = new ExerciseDemo();
		obj.test4();
	}
}
