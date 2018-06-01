package enable6;

import java.util.Random;

public class ThreadLocalTest2 {

	private static ThreadLocal<Userinfo> x = new ThreadLocal<Userinfo>();

	public static void main(String[] args) {
		for (int i = 0; i < 2; ++i) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + " has put data:" + data);
					Userinfo userinfo = new Userinfo();
					userinfo.setAge(data);
					userinfo.setName("hello");
					x.set(userinfo);
					new roll_in().get();
					new roll_out().get();
				}
			}).start();
		}
	}

	// 模拟汇款转入
	static class roll_in {
		public void get() {
			Userinfo data = x.get();
			System.out.println("A from:" + Thread.currentThread().getName() + " get data:" + data);
		}
	}

	// 模拟汇款转出
	static class roll_out {
		public void get() {
			Userinfo data = x.get();
			System.out.println("A from:" + Thread.currentThread().getName() + " get data:" + data);
		}
	}
}

class Userinfo {

	private String name;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Userinfo [name=" + name + ", age=" + age + "]";
	}
}
