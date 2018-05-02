package com.popokis.popok.http.extractor;

import io.undertow.server.HttpServerExchange;

public interface Extractor {
  /**
   * Extract the request payload in string format. The payload extraction is different depending on the type of the HTTP verb,
   * but also we can extract some parameters from the URL path.
   *
   * @param exchange The request object, undertow has the request and response in the same object, the exchange.
   * @return The String representation of the payload request.
   */
  String from(HttpServerExchange exchange);
}
