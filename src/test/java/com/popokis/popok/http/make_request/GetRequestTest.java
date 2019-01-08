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

class GetRequestTest {

  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }

  @Test
  void asyncGetRequestTest() {
    MakeRequest<CompletableFuture<HttpResponse<String>>> asyncGetRequest = new GetRequest<>(SimpleAsyncClient.getInstance());

    String payload = "";

    try {
      payload = asyncGetRequest.to(TESTING_SERVER.url() + "/fake/get", "key=popokis").get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @Test
  void syncGetRequestTest() {
    MakeRequest<String> syncPostRequest = new GetRequest<>(SimpleClient.getInstance());
    String payload = syncPostRequest.to(TESTING_SERVER.url() + "/fake/get", "key=popokis");

    assertTrue(payload.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    TESTING_SERVER.stop();
  }
}