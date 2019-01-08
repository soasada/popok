package com.popokis.popok.http.make_request;

import com.popokis.popok.http.client.SimpleAsyncClient;
import com.popokis.popok.http.client.SimpleClient;
import com.popokis.popok.http.server.SimpleServer;
import com.popokis.popok.util.http.TestingRouter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PostRequestTest {

  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }

  @Test
  void asyncPostRequestTest() {
    MakeRequest<CompletableFuture<HttpResponse<String>>> asyncPostRequest = new PostRequest<>(SimpleAsyncClient.getInstance());

    String payload = "";

    try {
      payload = asyncPostRequest.to(TESTING_SERVER.url() + "/fake/post", "popokis").get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @Test
  void syncPostRequestTest() {
    MakeRequest<String> syncPostRequest = new PostRequest<>(SimpleClient.getInstance());
    String payload = syncPostRequest.to(TESTING_SERVER.url() + "/fake/post", "popokis");

    assertTrue(payload.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    TESTING_SERVER.stop();
  }
}