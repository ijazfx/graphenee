package io.graphenee.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.util.callback.TRVoidCallback;

public class GxExecuteUtil {

	/**
	 * Purpose of this method is to divide a list in chunk and process sequentially.
	 */
	public static <T> void sequential(List<T> list, int chunkSize, TRParamCallback<List<T>> onIterate, TRVoidCallback onComplete, TRErrorCallback onError) {
		try {
			for (int from = 0, to = chunkSize; from < list.size(); from = to, to += chunkSize) {
				if (to > list.size()) {
					to = list.size();
				}
				List<T> chunck = list.subList(from, to);
				onIterate.execute(chunck);
			}
			onComplete.execute();
		} catch (Exception ex) {
			onError.execute(ex);
		}
	}

	/**
	 * Purpose of this method is to divide a list in chunk and process in parallel within a timeout.
	 */
	public static <T> void parallel(List<T> list, int chunkSize, TRParamCallback<List<T>> onIterate, TRVoidCallback onComplete, TRErrorCallback onError, long timeoutInMillis) {
		ExecutorService es = Executors.newCachedThreadPool();
		try {
			CountDownLatch latch = new CountDownLatch(list.size() / chunkSize);
			for (int from = 0, to = chunkSize; from < list.size(); from = to, to += chunkSize) {
				if (to > list.size()) {
					to = list.size();
				}
				List<T> chunck = list.subList(from, to);
				es.execute(() -> {
					onIterate.execute(chunck);
					latch.countDown();
				});
			}
			latch.await(timeoutInMillis, TimeUnit.MILLISECONDS);
			onComplete.execute();
		} catch (Exception ex) {
			onError.execute(ex);
		}
	}

}
