package com.popokis.popok.util.http;

import com.popokis.popok.http.extractor.GetExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import com.popokis.popok.http.router.Route;
import com.popokis.popok.http.server.SimpleServer;
import io.undertow.util.Methods;

import java.util.List;

public final class FakeServer {

  private static final String API_VERSION = "/api/v1";

  private static final String FAKE_GET = API_VERSION + "/fake/get";
  private static final String FAKE_POST = API_VERSION + "/fake/post";

  private final SimpleServer server;

  public FakeServer() {
    List<Route> routes = List.of(
        Route.of(Methods.GET, FAKE_GET, new FakeTextHandler(new GetExtractor())),
        Route.of(Methods.POST, FAKE_POST, new FakeTextHandler(new PostExtractor()))
    );

    server = new SimpleServer(8080, "0.0.0.0", routes);
  }

  public void stop() {
    server.stop();
  }

  public String url() {
    return server.url() + API_VERSION;
  }
}
