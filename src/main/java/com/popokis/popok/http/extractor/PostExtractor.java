package com.popokis.popok.http.extractor;

import com.popokis.popok.http.BodyReader;
import io.undertow.server.HttpServerExchange;

public final class PostExtractor implements Extractor {
  @Override
  public String from(HttpServerExchange exchange) {
    return BodyReader.asString(exchange.getInputStream());
  }
}
