package enable7;

public class MutilThreadShareData1 {
	public static void main(String[] args) {
		ShareData1 shareData = new ShareData1();
		new Thread(new MyRunnable1(shareData)).start();
		new Thread(new MyRunnable2(shareData)).start();
	}
}

class MyRunnable1 implements Runnable {

	private ShareData1 data;

	@Override
	public void run() {
		data.inc();
	}

	public MyRunnable1(ShareData1 data) {
		super();
		this.data = data;
	}

}

class MyRunnable2 implements Runnable {

	private ShareData1 data;

	@Override
	public void run() {
		data.des();
	}

	public MyRunnable2(ShareData1 data) {
		super();
		this.data = data;
	}
}

class ShareData1 {

	private int data = 0;

	public synchronized void inc() {
		data++;
	}

	public synchronized void des() {
		data--;
	}
}
