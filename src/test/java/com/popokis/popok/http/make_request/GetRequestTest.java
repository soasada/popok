package com.popokis.popok.http.make_request;

import com.popokis.popok.http.client.AsyncJsonClient;
import com.popokis.popok.http.client.JsonClient;
import com.popokis.popok.util.http.FakeServer;
import jdk.incubator.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GetRequestTest {

  private static FakeServer FAKE_SERVER;

  @BeforeAll
  static void initAll() {
    FAKE_SERVER = new FakeServer();
  }

  @Test
  void asyncGetRequestTest() {
    MakeRequest<CompletableFuture<HttpResponse<String>>> asyncGetRequest = new GetRequest<>(AsyncJsonClient.getInstance());

    String payload = "";

    try {
      payload = asyncGetRequest.to(FAKE_SERVER.url() + "/fake/get", "key=popokis").get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @Test
  void syncGetRequestTest() {
    MakeRequest<String> syncPostRequest = new GetRequest<>(JsonClient.getInstance());
    String payload = syncPostRequest.to(FAKE_SERVER.url() + "/fake/get", "key=popokis");

    assertTrue(payload.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    FAKE_SERVER.stop();
  }
}