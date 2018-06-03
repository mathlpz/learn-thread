package com.lpz.test.jvm;

import java.util.ArrayList;
import java.util.List;

public class G1Test {

	public static void main(String[] args) throws Exception {
		List<Byte[]> list = new ArrayList<Byte[]>();


		for (int i = 0; i < 130; i++) {
			System.out.println(i);
			list.add(new Byte[1024 * 1024]);
		}


		System.out.println("111111111111111111111");
		//刪除不连续的数据
		list.remove(0);
		list.remove(5);
		list.remove(8);
		list.remove(15);
		list.remove(20);
		System.out.println(list.get(0));
		System.out.println(list.size());


		System.out.println("222222222222222222222");


		Thread.sleep(1000);
		list.add(new Byte[1024 * 1024 * 2]);//存储的数据扩大1倍
		list.add(new Byte[1024 * 1024 * 2]);//存储的数据扩大1倍


		System.out.println("333333333333333333333");
		list.add(new Byte[1024 * 1024]);
		System.out.println("44444444444444444444");
		
	}
	
	
}
