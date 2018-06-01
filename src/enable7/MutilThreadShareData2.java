package enable7;

public class MutilThreadShareData2 {

	private int data = 0;

	public synchronized void inc() {
		data++;
	}

	public synchronized void des() {
		data--;
	}

	public void init() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				inc();
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				des();
			}
		}).start();
	}

	public static void main(String[] args) {
		MutilThreadShareData2 opbj = new MutilThreadShareData2();
		opbj.init();
	}
}
