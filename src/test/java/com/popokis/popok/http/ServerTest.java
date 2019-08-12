package com.popokis.popok.http;

import io.undertow.Handlers;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.Test;

class ServerTest {

  @Test
  void shouldReturnHelloWorldResponse() {
    Server.builder(
        Handlers.path()
            .addPrefixPath("/api/v1", Handlers.routing()
                .get("/hello", (exchange) -> {
                  exchange.setStatusCode(StatusCodes.OK);
                  exchange.getResponseSender().send("Hello World!");
                })
            )
    )
        .build().start();
  }
}