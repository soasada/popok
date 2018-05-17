package com.popokis.popok.util.http;

import com.popokis.popok.http.extractor.GetExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import com.popokis.popok.http.router.Route;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;

import java.util.List;

public final class FakeServer {

  private static final String API_VERSION = "/api/v1";

  private static final String FAKE_GET = API_VERSION + "/fake/get";
  private static final String FAKE_POST = API_VERSION + "/fake/post";

  private static final int PORT = 8080;
  private static final String URL = "0.0.0.0";

  private final Undertow server;
  private final List<Route> routes;
  private final HttpHandler routerHandler;

  public FakeServer() {
    routes = List.of(
        Route.of(Methods.GET, FAKE_GET, new FakeTextHandler(new GetExtractor())),
        Route.of(Methods.POST, FAKE_POST, new FakeTextHandler(new PostExtractor())));

    RoutingHandler tempHandler = new RoutingHandler();
    for (var route : routes) {
      tempHandler.add(route.verb(), route.endpoint(), route.handler());
    }

    routerHandler = Handlers.routing().addAll(tempHandler);

    server = Undertow.builder()
        .addHttpListener(PORT, URL, routerHandler)
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
