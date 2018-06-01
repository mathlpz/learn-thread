package enable19;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionModifyExceptionTest {

	public void test1() {

		Collection<User> users = new ArrayList<User>();

		users.add(new User("张三", 28));
		users.add(new User("李四", 25));
		users.add(new User("王五", 31));
		Iterator<User> itrUsers = users.iterator();
		while (itrUsers.hasNext()) {// 移除元素后hasNext()一直返回true，为什么没有死循环
			User user = (User) itrUsers.next();// 抛出异常
			if ("张三".equals(user.getName())) {
				users.remove(user);
				System.out.println("成功删除了" + user);
				// break;
			}
		}
	}

	public void test2() {

		Collection<User> users = new CopyOnWriteArrayList<User>();

		users.add(new User("张三", 28));
		users.add(new User("李四", 25));
		users.add(new User("王五", 31));
		Iterator<User> itrUsers = users.iterator();
		while (itrUsers.hasNext()) {
			User user = (User) itrUsers.next();// 抛出异常
			if ("张三".equals(user.getName())) {
				users.remove(user);
				System.out.println("成功删除了" + user);
			}
		}
	}

	public static void main(String[] args) {
		CollectionModifyExceptionTest obj = new CollectionModifyExceptionTest();
		obj.test2();
	}
}

class User implements Cloneable {
	private String name;
	private int age;

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User user = (User) obj;
		// if(this.name==user.name && this.age==user.age)
		if (this.name.equals(user.name) && this.age == user.age) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return name.hashCode() + age;
	}

	public String toString() {
		return "{name:'" + name + "',age:" + age + "}";
	}

	public Object clone() {
		Object object = null;
		try {
			object = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return object;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}
}
