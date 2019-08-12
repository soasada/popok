package com.popokis.popok.http;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.StatusCodes;

public final class TestRouter {

  private TestRouter() {}

  public static HttpHandler of() {
    return Handlers.path()
        .addPrefixPath("/api/v1", Handlers.routing()
            .get("/hello", (exchange) -> {
              exchange.setStatusCode(StatusCodes.OK);
              exchange.getResponseSender().send("Hello World!");
            })
        );
  }
}
