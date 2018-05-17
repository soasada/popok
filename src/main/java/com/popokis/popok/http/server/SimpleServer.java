package com.popokis.popok.http.server;

import com.popokis.popok.http.router.Route;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

import java.util.List;

public final class SimpleServer {

  private final int port;
  private final String host;
  private final Undertow server;

  public SimpleServer(int port, String host, List<Route> routes) {
    this.port = port;
    this.host = host;

    RoutingHandler tempHandler = new RoutingHandler();
    for (var route : routes) {
      tempHandler.add(route.verb(), route.endpoint(), route.handler());
    }

    server = Undertow.builder()
        .addHttpListener(port, host, Handlers.routing().addAll(tempHandler))
        .build();

    server.start();
  }

  public void stop() {
    server.stop();
  }

  public String url() {
    return "http://" + host + ":" + port;
  }
}
