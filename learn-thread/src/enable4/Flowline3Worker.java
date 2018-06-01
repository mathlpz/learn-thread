package enable4;

public class Flowline3Worker {

	public static void main(String[] args) {

		final Business business = new Business();
		business.setNextStep(1);

		final int productSum = 40;

		// 子线程1
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					business.step1(i);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					business.step2(i);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= productSum; ++i) {
					business.step3(i);
				}
			}
		}).start();
	}
}

class Business {

	private volatile int nextStep = 1;

	public void setNextStep(int nextStep) {
		this.nextStep = nextStep;
	}

	public synchronized void step1(int i) {
		while (1 != nextStep) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int j = 1; j <= 10; ++j) {
			System.out.println("step1 Thread of sequence of " + j + "\tLoop of\t" + i);
		}
		System.out.println("===========1 finish==========");
		nextStep = 2;
		this.notifyAll();
	}

	public synchronized void step2(int i) {
		while (2 != nextStep) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int j = 1; j <= 20; ++j) {
			System.out.println("step2 Thread of sequrence of " + j + "\tLoop of\t" + i);
		}
		System.out.println("===========2 finish==========");
		nextStep = 3;
		this.notifyAll();
	}

	public synchronized void step3(int i) {
		while (3 != nextStep) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int j = 1; j <= 30; ++j) {
			System.out.println("step3 Thread of sequrence of " + j + "\tLoop of\t" + i);
		}
		System.out.println("===========3 finish==========");
		nextStep = 1;
		this.notifyAll();
	}
}
