package com.popokis.popok.http.router;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HttpString;

public final class Route {

  private final HttpString verb;
  private final String endpoint;
  private final HttpHandler handler;

  public Route(HttpString verb, String endpoint, HttpHandler handler) {
    this.verb = verb;
    this.endpoint = endpoint;
    this.handler = new BlockingHandler(handler);
  }

  public HttpString verb() {
    return verb;
  }

  public String endpoint() {
    return endpoint;
  }

  public HttpHandler handler() {
    return handler;
  }
}
