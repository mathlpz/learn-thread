package enable10;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallAndFutureTest {
	public static void main(String[] args) {

		// Callable 和 Future
		ExecutorService threadPool2 = Executors.newSingleThreadExecutor();

		Future<String> future = threadPool2.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("准备上传到sftp....");
				Thread.sleep(4000);
				System.out.println("完成上传到sftp");
				return "hello";
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					System.out.println("上传到数据库....");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		System.out.println("等待结果");

		try {
			System.out.println(future.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		//
		try {
			System.out.println(future.get(10, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		threadPool2.shutdown();

	}
}
