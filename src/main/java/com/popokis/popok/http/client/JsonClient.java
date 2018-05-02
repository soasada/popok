package com.popokis.popok.http.client;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public final class JsonClient implements Client<String> {

  private final Duration timeout;

  public JsonClient() {
    this(Duration.ofMinutes(1));
  }

  public JsonClient(Duration timeout) {
    this.timeout = timeout;
  }

  public String get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .build();

    return httpRequest(request);
  }

  public String post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublisher.fromString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private String httpRequest(HttpRequest request) {
    HttpClient client = HttpClient.newHttpClient();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());
      return response.body();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
