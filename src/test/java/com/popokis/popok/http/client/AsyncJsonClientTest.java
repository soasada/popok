package com.popokis.popok.http.client;

import com.popokis.popok.util.http.FakeServer;
import jdk.incubator.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncJsonClientTest {

  private static Client<CompletableFuture<HttpResponse<String>>> ASYNC_CLIENT;
  private static FakeServer FAKE_SERVER;

  @BeforeAll
  static void initAll() {
    ASYNC_CLIENT = AsyncJsonClient.getInstance();
    FAKE_SERVER = new FakeServer();
  }

  @Test
  void asyncGetTest() {
    CompletableFuture<HttpResponse<String>> response = ASYNC_CLIENT.get(FAKE_SERVER.url() + "/fake/get?key=popokis");

    String payload = "";

    try {
      payload = response.get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @Test
  void asyncPostTest() {
    CompletableFuture<HttpResponse<String>> response = ASYNC_CLIENT.post(FAKE_SERVER.url() + "/fake/post", "popokis");
    String payload = "";

    try {
      payload = response.get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    ASYNC_CLIENT = null;
    FAKE_SERVER.stop();
  }
}