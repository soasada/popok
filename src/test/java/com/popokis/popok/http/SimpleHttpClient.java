/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popokis.popok.http;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Properties;

public final class SimpleHttpClient {

  private final Duration timeout;
  private final HttpClient httpClient;

  private SimpleHttpClient() {
    try {
      final Properties props = System.getProperties();
      props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
      SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
      sslContext.init(null, trustAllCerts, new SecureRandom());
      this.timeout = Duration.ofMinutes(2);
      this.httpClient = HttpClient.newBuilder()
          .connectTimeout(timeout)
          .sslContext(sslContext)
          .followRedirects(HttpClient.Redirect.ALWAYS)
          .build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  private static class Holder {
    private static final SimpleHttpClient INSTANCE = new SimpleHttpClient();
  }

  public static SimpleHttpClient getInstance() {
    return Holder.INSTANCE;
  }

  public HttpResponse<String> get(String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .GET()
        .build();

    return httpRequest(request);
  }

  public HttpResponse<String> post(String url, String jsonBody) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(timeout)
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    return httpRequest(request);
  }

  private HttpResponse<String> httpRequest(HttpRequest request) {
    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return response;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static TrustManager[] trustAllCerts = new TrustManager[]{
      new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      }
  };
}
