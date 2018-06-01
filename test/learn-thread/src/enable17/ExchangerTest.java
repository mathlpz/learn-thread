package enable17;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {

	public static void main(String[] args) {

		ExecutorService service = Executors.newCachedThreadPool();
		
		final Exchanger<String> exchange = new Exchanger<String>();

		//
		service.execute(new Runnable() {

			@Override
			public void run() {
				try {
					String data1 = "武器";
					Thread.sleep((long) (Math.random() * 1000));
					System.out.println("线程" + Thread.currentThread().getName() + "正在把" + data1 + "交换出去");
					String data2 = exchange.exchange(data1);
					System.out.println("线程" + Thread.currentThread().getName() + "换回的东西为:" + data2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//
		service.execute(new Runnable() {

			@Override
			public void run() {
				try {
					String data1 = "美元";
					Thread.sleep((long) (Math.random() * 1000));
					System.out.println("线程" + Thread.currentThread().getName() + "正在把" + data1 + "交换出去");
					String data2 = exchange.exchange(data1);
					System.out.println("线程" + Thread.currentThread().getName() + "换回的东西为:" + data2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		service.shutdown();
	}
}
