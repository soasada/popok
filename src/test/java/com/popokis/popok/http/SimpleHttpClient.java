package com.popokis.popok.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class SimpleHttpClient {

  private final Duration timeout;
  private final HttpClient httpClient;

  private SimpleHttpClient() {
    this.timeout = Duration.ofMinutes(2);
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(timeout)
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();
  }

  private static class Holder {
    private static final SimpleHttpClient INSTANCE = new SimpleHttpClient();
  }

  public static SimpleHttpClient getInstance() {
    return Holder.INSTANCE;
  }

  public String get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .GET()
        .build();

    return httpRequest(request);
  }

  public String post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
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
