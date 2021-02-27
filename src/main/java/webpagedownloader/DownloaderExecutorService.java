package webpagedownloader;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class DownloaderExecutorService {
	private final ExecutorService executorService;
	private final ConcurrentLinkedQueue<Future<Void>> futures;

	public DownloaderExecutorService() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		futures = new ConcurrentLinkedQueue<>();
	}

	public DownloaderExecutorService(int nbrOfThreads) {
		executorService = Executors.newFixedThreadPool(nbrOfThreads);
		futures = new ConcurrentLinkedQueue<>();
	}

	public Future submit(Runnable r) {
		return executorService.submit(r);
	}

	public void addFuture(Future<Void> future) {
		futures.add(future);
	}

	public void waitUntilComplete() {
		while (!futures.isEmpty()) {
			Future<Void> future = futures.remove();
			try {
				future.get(30, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				addFuture(future);
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		executorService.shutdown();
	}
}
