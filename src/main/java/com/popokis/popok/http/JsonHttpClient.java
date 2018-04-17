package com.popokis.popok.http;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

public final class JsonHttpClient {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(1);

  private JsonHttpClient() {}

  public static String get(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(DEFAULT_TIMEOUT)
        .build();

    return httpRequest(request);
  }

  public static String post(String url, String jsonBody) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(DEFAULT_TIMEOUT)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublisher.fromString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private static String httpRequest(HttpRequest request) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());

    return response.body();
  }
}
