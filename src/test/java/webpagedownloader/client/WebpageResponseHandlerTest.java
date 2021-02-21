package webpagedownloader.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebpageResponseHandlerTest {

	private WebpageResponseHandler responseHandler;

	@BeforeEach
	public void setup() {
		responseHandler = new WebpageResponseHandler();
	}

	@Test
	public void handleNormalResponse() throws IOException {
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream("test payload".getBytes()));
		response.setEntity(entity);

		String actual = responseHandler.handleResponse(response);

		assertEquals(actual, "test payload");

	}

	@Test
	public void handle404Response() {
		HttpResponse response = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 404, "OK"));
		Assertions.assertThrows(ClientProtocolException.class, () -> responseHandler.handleResponse(response));
	}
}
