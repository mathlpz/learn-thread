package com.lpz.test.jvm;

/**
 * 可触及的
	从根节点可以触及到这个对象
可复活的
	一旦所有引用被释放，就是可复活状态
	因为在finalize()中可能复活该对象
不可触及的
	在finalize()后，可能会进入不可触及状态
	不可触及的对象不可能复活
	可以回收
 *
 * 经验：避免使用finalize()，操作不慎可能导致错误。
 * 优先级低，何时被调用， 不确定
	   何时发生GC不确定
 * 可以使用try-catch-finally来替代它
 * 
 * @author lpz
 *
 */
public class CanReliveObj {
	
	public static CanReliveObj obj;
 

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	    System.out.println("CanReliveObj finalize called");
	    // 回收时，重新给对象添加引用
	    obj = this;
	}
	
	@Override
	public String toString(){
	    return "I am CanReliveObj";
	}
	
	
	public static void main(String[] args) throws InterruptedException{
		obj = new CanReliveObj();
		obj = null;   //可复活
		// 調用對象的finalize方法，回收對象
		System.gc();
		Thread.sleep(1000);
		if (obj == null) {
		   System.out.println("obj 是 null");
		} else {
		   System.out.println("obj 可用");
		}
		System.out.println("第二次gc");
		obj = null;    //不可复活
		System.gc();
		Thread.sleep(1000);
		if(obj == null){
			System.out.println("obj 是 null");
		} else {
			System.out.println("obj 可用");
		}
	}

	
	
}
