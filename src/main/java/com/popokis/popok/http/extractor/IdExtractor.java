package com.popokis.popok.http.extractor;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;

public final class IdExtractor implements Extractor {
  @Override
  public String from(HttpServerExchange exchange) {
    PathTemplateMatch pathMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
    return pathMatch.getParameters().get("id");
  }
}
