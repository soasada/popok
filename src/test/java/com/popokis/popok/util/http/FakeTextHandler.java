package com.popokis.popok.util.http;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public final class FakeTextHandler implements HttpHandler {

  private final Extractor extractor;

  public FakeTextHandler(Extractor extractor) {
    this.extractor = extractor;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    ResponseSender.asText(exchange, extractor.from(exchange));
  }
}
