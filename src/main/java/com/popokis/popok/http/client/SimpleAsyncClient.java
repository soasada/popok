package com.popokis.popok.http.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class SimpleAsyncClient implements Client<CompletableFuture<HttpResponse<String>>> {

  private final Duration timeout;
  private final HttpClient httpClient;

  private SimpleAsyncClient() {
    this.timeout = Duration.ofMinutes(2);
    this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
  }

  private static class Holder {
    private static final Client<CompletableFuture<HttpResponse<String>>> INSTANCE = new SimpleAsyncClient();
  }

  public static Client<CompletableFuture<HttpResponse<String>>> getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public CompletableFuture<HttpResponse<String>> get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .build();

    return httpRequest(request);
  }

  @Override
  public CompletableFuture<HttpResponse<String>> post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private CompletableFuture<HttpResponse<String>> httpRequest(HttpRequest request) {
    return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }
}
