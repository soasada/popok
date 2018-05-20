package com.popokis.popok.http;

import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.util.ExceptionUtils;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;

import static com.popokis.popok.util.ExceptionUtils.errorResponse;
import static com.popokis.popok.util.ExceptionUtils.logException;

public final class ResponseSender {

  private ResponseSender() {}

  public static void asJson(HttpServerExchange exchange, String requestId, Throwable e, Logger logger) {
    logException(e, requestId, logger);
    asJson(exchange, errorResponse(requestId, "KO", ExceptionUtils.getRootCause(e).getMessage()));
  }

  public static void asJson(HttpServerExchange exchange, String requestId, RestResponse response) {
    asJson(exchange, errorResponse(requestId, response.response().code(), response.response().message()));
  }

  public static void asJson(HttpServerExchange exchange, String response) {
    exchange.setStatusCode(StatusCodes.OK);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
    exchange.getResponseSender().send(response);
  }

  public static void asText(HttpServerExchange exchange, String response) {
    exchange.setStatusCode(StatusCodes.OK);
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
    exchange.getResponseSender().send(response);
  }
}
