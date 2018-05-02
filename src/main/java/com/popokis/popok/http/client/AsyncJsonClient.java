package com.popokis.popok.http.client;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class AsyncJsonClient implements Client<CompletableFuture<HttpResponse<String>>> {

  private final Duration timeout;

  public AsyncJsonClient() {
    this(Duration.ofMinutes(1));
  }

  public AsyncJsonClient(Duration timeout) {
    this.timeout = timeout;
  }

  public CompletableFuture<HttpResponse<String>> get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .build();

    return httpRequest(request);
  }

  public CompletableFuture<HttpResponse<String>> post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublisher.fromString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private CompletableFuture<HttpResponse<String>> httpRequest(HttpRequest request) {
    HttpClient client = HttpClient.newHttpClient();
    return client.sendAsync(request, HttpResponse.BodyHandler.asString());
  }
}
