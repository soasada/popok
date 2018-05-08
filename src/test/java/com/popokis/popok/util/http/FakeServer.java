package com.popokis.popok.util.http;

import com.popokis.popok.http.extractor.GetExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;

public final class FakeServer {

  private static final String API_VERSION = "/api/v1";
  private static final int PORT = 8080;
  private static final String URL = "0.0.0.0";
  private final Undertow server;

  public FakeServer() {
    RoutingHandler routes = new RoutingHandler();
    routes
        .get(API_VERSION + "/fake/get", new BlockingHandler(new FakeTextHandler(new GetExtractor())))
        .post(API_VERSION + "/fake/post", new BlockingHandler(new FakeTextHandler(new PostExtractor())));

    server = Undertow.builder()
        .addHttpListener(PORT, URL)
        .setHandler(routes)
        .build();

    server.start();
  }

  public void stop() {
    server.stop();
  }

  public String url() {
    return "http://" + URL + ":" + PORT + API_VERSION;
  }
}
