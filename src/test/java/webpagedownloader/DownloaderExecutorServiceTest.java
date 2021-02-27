package webpagedownloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DownloaderExecutorServiceTest {

	DownloaderExecutorService downloaderExecutorService;

	@BeforeEach
	public void setup() {
		downloaderExecutorService = new DownloaderExecutorService();
	}

	@Test
	public void testWaitUntilComplete() throws InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<Void> future1 = new CompletableFuture<Void>();
		CompletableFuture<Void> future2 = new CompletableFuture<Void>();

		downloaderExecutorService.addFuture(future1);
		downloaderExecutorService.addFuture(future2);

		Future wait = downloaderExecutorService.submit(() -> downloaderExecutorService.waitUntilComplete());

		assertFalse(wait.isDone());
		future1.complete(null);
		assertFalse(wait.isDone());
		future2.complete(null);
		wait.get(10, TimeUnit.SECONDS);
		assertTrue(wait.isDone());
	}
}
