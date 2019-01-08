package com.popokis.popok.http.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class SimpleClient implements Client<String> {

  private final Duration timeout;
  private final HttpClient httpClient;

  private SimpleClient() {
    this.timeout = Duration.ofMinutes(2);
    this.httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
  }

  private static class Holder {
    private static final Client<String> INSTANCE = new SimpleClient();
  }

  public static Client<String> getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public String get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .GET()
        .build();

    return httpRequest(request);
  }

  @Override
  public String post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private String httpRequest(HttpRequest request) {
    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
