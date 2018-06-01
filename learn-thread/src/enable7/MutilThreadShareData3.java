package enable7;

public class MutilThreadShareData3 {

	public void handle() {

		// 也可以改为MutilThreadShareData3的成员变量
		final ShareData3 shareData = new ShareData3();

		new Thread(new Runnable() {

			@Override
			public void run() {
				shareData.inc();

			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				shareData.des();
			}
		}).start();
	}

	public static void main(String[] args) {
		MutilThreadShareData3 obj = new MutilThreadShareData3();
		obj.handle();
	}
}

class ShareData3 {

	private int data = 0;

	public synchronized void inc() {
		data++;
	}

	public synchronized void des() {
		data--;
	}
}
