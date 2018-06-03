package com.lpz.test.jvm;

/**
 * 
 * 堆区:
1.存储的全部是对象，每个对象都包含一个与之对应的class的信息。(class的目的是得到操作指令)
2.jvm只有一个堆区(heap)被所有线程共享，堆中不存放基本类型和对象引用，只存放对象本身
栈区:
1.每个线程包含一个栈区，栈中只保存基础数据类型的对象和自定义对象的引用(不是对象)，对象都存放在堆区中
2.每个栈中的数据(原始类型和对象引用)都是私有的，其他栈不能访问。
3.栈分为3个部分：基本类型变量区、执行环境上下文、操作指令区(存放操作指令)。
方法区:
1.又叫静态区，跟堆一样，被所有的线程共享。方法区包含所有的class和static变量。
2.方法区中包含的都是在整个程序中永远唯一的元素，如class，static变量。
 * 
 * 
 * 系统收到了我们发出的指令，启动了一个Java虚拟机进程，这个进程首先从classpath中找到AppMain.class文件，
 * 读取这个文件中的二进制数据，然后把Appmain类的类信息存放到运行时数据区的方法区中。
 * 这一过程称为AppMain类的加载过程。
	接着，Java虚拟机定位到方法区中AppMain类的Main()方法的字节码，开始执行它的指令。
	这个main()方法的第一条语句就是：
	Sample test1=new Sample("测试1");
	语句很简单啦，就是让java虚拟机创建一个Sample实例，并且呢，使引用变量test1引用这个实例。
	貌似小case一桩哦，就让我们来跟踪一下Java虚拟机，看看它究竟是怎么来执行这个任务的：
	1、 Java虚拟机一看，不就是建立一个Sample实例吗，简单，于是就直奔方法区而去，先找到Sample类的类型信息再说。
	结果呢，嘿嘿，没找到@@， 这会儿的方法区里还没有Sample类呢。可Java虚拟机也不是一根筋的笨蛋，
	于是，它发扬“自己动手，丰衣足食”的作风，立马加载了Sample类， 把Sample类的类型信息存放在方法区里。
	2、 好啦，资料找到了，下面就开始干活啦。Java虚拟机做的第一件事情就是在堆区中为一个新的Sample实例分配内存, 
	这个Sample实例持有着指向方法区的Sample类的类型信息的引用。这里所说的引用，实际上指的是Sample类的类型信息在方法区中的内存地址， 
	其实，就是有点类似于C语言里的指针啦~~，而这个地址呢，就存放了在Sample实例的数据区里。
	3、 在JAVA虚拟机进程中，每个线程都会拥有一个方法调用栈，用来跟踪线程运行中一系列的方法调用过程，栈中的每一个元素就被称为栈帧，
	每当线程调用一个方 法的时候就会向方法栈压入一个新帧。这里的帧用来存储方法的参数、局部变量和运算过程中的临时数据。OK，原理讲完了，
	就让我们来继续我们的跟踪行动！位 于“=”前的Test1是一个在main()方法中定义的变量，可见，它是一个局部变量，
	因此，它被会添加到了执行main()方法的主线程的JAVA方 法调用栈中。而“=”将把这个test1变量指向堆区中的Sample实例，
	也就是说，它持有指向Sample实例的引用。
	OK，到这里为止呢，JAVA虚拟机就完成了这个简单语句的执行任务。参考我们的行动向导图，我们终于初步摸清了JAVA虚拟机的一点点底细了，COOL！
	接下来，JAVA虚拟机将继续执行后续指令，在堆区里继续创建另一个Sample实例，然后依次执行它们的printName()方法。
	当JAVA虚拟机 执行test1.printName()方法时，JAVA虚拟机根据局部变量test1持有的引用，定位到堆区中的Sample实例，
	再根据Sample 实例持有的引用，定位到方法去中Sample类的类型信息，从而获得printName()方法的字节码，
	接着执行printName()方法包含的指 令。
 * @author lpz
 *
 */
//运行时, jvm 把appmain的信息都放入方法区
public class AppMain {
    //main 方法本身放入方法区。
	public static void main(String[] args){
		Sample test1 = new  Sample( " 测试1 " );   //test1是引用，所以放到栈区里， Sample是自定义对象应该放到堆里面
		Sample test2 = new  Sample( " 测试2 " );
		test1.printName();
		test2.printName();
	}
} 

//运行时, jvm 把appmain的信息都放入方法区
class  Sample {

	/** 范例名称 */
	private String name;      //new Sample实例后， name 引用放入栈区里，  name 对象放入堆里
	
	/** 构造方法 */
	public  Sample(String name){
		this.name = name;
	}
	
	//print方法本身放入 方法区里。
	/** 输出 */
	public void printName() {
		System.out.println(name);
	}

} 
