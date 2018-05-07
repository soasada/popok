package com.popokis.popok.http;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public final class ResponseSender {

  private ResponseSender() {}

  public static void asJson(HttpServerExchange exchange, String response) {
    exchange.setStatusCode(StatusCodes.OK);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
    exchange.getResponseSender().send(response);
  }
}
