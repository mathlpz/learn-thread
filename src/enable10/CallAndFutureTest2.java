package enable10;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallAndFutureTest2 {

	public void test1() throws InterruptedException, ExecutionException {

		ExecutorService threadPool = Executors.newFixedThreadPool(10);

		CompletionService<String> completionService = new ExecutorCompletionService<String>(threadPool);

		// 种了10块麦田
		for (int i = 0; i < 10; i++) {
			final int seq = i;
			completionService.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					Thread.sleep(new Random().nextInt(5000));
					return "第" + seq + "块麦田成熟了";
				}
			});
		}

		// 等待收割
		for (int i = 0; i < 10; i++) {
			String seq = completionService.take().get();
			System.out.println(seq + ",收割成功");
		}
		threadPool.shutdown();

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CallAndFutureTest2 obj = new CallAndFutureTest2();
		obj.test1();
	}
}
