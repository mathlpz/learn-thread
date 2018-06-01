package enable6;

import java.util.Random;

public class ThreadLocalTest3 {

	public static void main(String[] args) {
		for (int i = 0; i < 2; ++i) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + " has put data:" + data);
					//
					MyThreadScopeData.getThreadInstance().setName("name" + data);
					MyThreadScopeData.getThreadInstance().setAge(data);
					//
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public void get() {
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("A from:" + Thread.currentThread().getName() + " getMyData: " + myData.getName() + ","
					+ myData.getAge());
		}
	}

	static class B {
		public void get() {
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("B from:" + Thread.currentThread().getName() + " getMyData: " + myData.getName() + ","
					+ myData.getAge());
		}
	}

}

// 和单例模式类似，但每个实例的得到的对象是不同的
class MyThreadScopeData {

	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();

	// 因为每个线程都是不同的实例，所以不需要同步
	public static MyThreadScopeData getThreadInstance() {// 任意一个线程调用该方法，就会得到该线程的对应实例对象

		// 实例与每个线程相关
		MyThreadScopeData instance = map.get();

		if (instance == null) {
			System.out.println("MyThreadScopeData---instance == null");
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		return instance;
	}

	private MyThreadScopeData() {

	}

	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
