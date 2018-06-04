package com.popokis.popok.http.handler;

import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manipulator.BasicManipulator;
import com.popokis.popok.log.context.LoggerContext;
import com.popokis.popok.service.Service;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;

abstract class AbstractHandler<Req, Res> implements HttpHandler {

  protected final Extractor extractor;
  protected final Logger logger;
  protected final Service<Req, Res> service;

  AbstractHandler(Extractor extractor, Logger logger, Service<Req, Res> service) {
    this.extractor = extractor;
    this.logger = logger;
    this.service = service;
  }

  protected abstract Req deserializeRequest(String requestPayload);

  protected abstract void validate(Req request);

  protected Req manipulateRequest(Req request) {
    return new BasicManipulator<Req>().manipulate(request);
  }

  protected final void logRequest(HttpServerExchange exchange, String requestPayload) {
    LoggerContext.addToContext("request-serviceURL", exchange.getRequestURL());
    LoggerContext.addToContext("request-payload", requestPayload);
    logger.info("Incoming request");
  }

  protected final void logResponse(String response) {
    LoggerContext.addToContext("response", response);
    logger.info("Response");
    LoggerContext.clearContext();
  }
}
