package com.popokis.popok.http.client;

import jdk.incubator.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncJsonClientTest {

  private static Client<CompletableFuture<HttpResponse<String>>> ASYNC_CLIENT;

  @BeforeAll
  static void initAll() {
    ASYNC_CLIENT = AsyncJsonClient.getInstance();
  }

  @Test
  public void asyncGetTest() {
    CompletableFuture<HttpResponse<String>> response = ASYNC_CLIENT.get("https://httpbin.org/get?id=popokis");

    String payload = "";

    try {
      payload = response.get().body();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    assertTrue(payload.contains("popokis"));
  }

  @Test
  public void asyncPostTest() {
    CompletableFuture<HttpResponse<String>> response = ASYNC_CLIENT.post("https://httpbin.org/post", "popokis");
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
  }
}