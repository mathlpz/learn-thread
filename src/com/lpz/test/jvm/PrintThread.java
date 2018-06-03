package com.lpz.test.jvm;

public class PrintThread extends Thread{
	
	public static final long starttime = System.currentTimeMillis();
	
	@Override
	public void run(){
		try{
			while(true){
				long t = System.currentTimeMillis() - starttime;
				System.out.println("time:"+t);
				Thread.sleep(100);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PrintThread t = new PrintThread();
		t.start();
	}
	
}
