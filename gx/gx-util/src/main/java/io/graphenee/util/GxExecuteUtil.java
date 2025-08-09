package io.graphenee.util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRParamCallback;
import io.graphenee.util.callback.TRVoidCallback;

/**
 * A utility class for executing code.
 */
public class GxExecuteUtil {

	/**
	 * Creates a new instance of this utility class.
	 */
	public GxExecuteUtil() {
		// a default constructor
	}

	/**
	 * Purpose of this method is to divide a list in chunk and process sequentially.
	 * @param <T> The type of the list.
	 * @param list The list to process.
	 * @param chunkSize The size of the chunks.
	 * @param onIterate The callback to execute on each chunk.
	 * @param onComplete The callback to execute when the process is complete.
	 * @param onError The callback to execute when an error occurs.
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
	 * @param <T> The type of the list.
	 * @param list The list to process.
	 * @param chunkSize The size of the chunks.
	 * @param onIterate The callback to execute on each chunk.
	 * @param onComplete The callback to execute when the process is complete.
	 * @param onError The callback to execute when an error occurs.
	 * @param timeoutInMillis The timeout in milliseconds.
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
