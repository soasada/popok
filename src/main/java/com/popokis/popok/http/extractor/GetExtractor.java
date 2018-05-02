package com.popokis.popok.http.extractor;

import io.undertow.server.HttpServerExchange;

public final class GetExtractor implements Extractor {
  @Override
  public String from(HttpServerExchange exchange) {
    return exchange.getQueryString();
  }
}
