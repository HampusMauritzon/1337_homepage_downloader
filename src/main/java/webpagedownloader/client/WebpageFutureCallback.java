package webpagedownloader.client;

import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import webpagedownloader.DownloaderExecutorService;

import java.net.URI;
import java.util.concurrent.FutureTask;
import java.util.function.BiConsumer;

public class WebpageFutureCallback implements FutureCallback<SimpleHttpResponse> {
	private final DownloaderExecutorService executorService;
	private final Task task;
	private final ResponseProcess<URI, String> responseProcess;

	public WebpageFutureCallback(DownloaderExecutorService executorService, URI url, BiConsumer<URI, String> dataConsumer) {
		this.executorService = executorService;
		responseProcess = new ResponseProcess(dataConsumer, url);
		task = new Task(responseProcess);
		executorService.addFuture(task);
	}

	@Override
	public void completed(SimpleHttpResponse simpleHttpResponse) {
		responseProcess.setDelayedInput(simpleHttpResponse.getBodyText());
		executorService.submit(task);
	}

	@Override
	public void failed(Exception e) {
		e.printStackTrace();
		task.setException(e);
	}

	@Override
	public void cancelled() {
		task.cancel(false);
	}

	private static class Task extends FutureTask<Void> {
		public Task(Runnable runnable) {
			super(runnable, null);
		}

		public void setException(Exception e) {
			super.setException(e);
		}
	}

	private static class ResponseProcess<T, K> implements Runnable {
		private final BiConsumer<T, K> consumer;
		private final T firstInput;
		private K delayedInput;

		public ResponseProcess(BiConsumer<T, K> consumer, T firstInput) {
			this.consumer = consumer;
			this.firstInput = firstInput;
		}

		public void setDelayedInput(K delayedInput) {
			this.delayedInput = delayedInput;
		}

		@Override
		public void run() {
			consumer.accept(firstInput, delayedInput);
		}
	}
}
