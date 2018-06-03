package com.popokis.popok.http.handler.http;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;

public abstract class AbstractServiceSyncHandler<Req extends Identifiable, Res> extends AbstractServiceHandler<Req, Res, Res> {

  public AbstractServiceSyncHandler(Extractor extractor, Logger logger, Service<Req, Res> service) {
    super(extractor, logger, service);
  }

  @Override
  protected final void finalizeResponse(Res response, HttpServerExchange exchange) {
    ResponseSender.asJson(exchange, manageResponse(response, exchange));
  }

  @Override
  public final void handleRequest(HttpServerExchange exchange) {
    Res response = manageRequest(exchange);
    finalizeResponse(response, exchange);
  }
}
