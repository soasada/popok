package com.popokis.popok.http.manager;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.log.context.LoggerContext;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identificable;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;

public final class RequestManager<R, S> implements Manager<HttpServerExchange, PopokExchange<S>> {

  private final Extractor extractor;
  private final Logger logger;
  private final Deserializator<R, String> requestDeserializator;
  private final Validator<R> requestValidator;
  private final Manipulator<R> requestManipulator;
  private final Service<R, S> service;
  private final Identificable<R> requestIdentificable;

  public RequestManager(Extractor extractor,
                        String loggerName,
                        Deserializator<R, String> requestDeserializator,
                        Validator<R> requestValidator,
                        Manipulator<R> requestManipulator,
                        Service<R, S> service,
                        Identificable<R> requestIdentificable) {
    this.extractor = extractor;
    this.logger = PopokLogger.getLogger(loggerName);
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
    this.requestManipulator = requestManipulator;
    this.service = service;
    this.requestIdentificable = requestIdentificable;
  }

  @Override
  public PopokExchange<S> manage(HttpServerExchange exchange) {
    String requestPayload = extractor.from(exchange);
    logRequest(exchange, requestPayload);
    String requestId = "-1";

    try {
      R deserializedRequest = requestDeserializator.deserialize(requestPayload);
      requestId = requestIdentificable.from(deserializedRequest);
      requestValidator.validate(deserializedRequest);
      R manipulatedRequest = requestManipulator.manipulate(deserializedRequest);
      S response = service.call(manipulatedRequest);
      return PopokExchange.create(requestId, response);
    } catch (Exception e) {
      ResponseSender.asJson(exchange, requestId, e, logger);
      throw new RuntimeException(e);
    }
  }

  private void logRequest(HttpServerExchange exchange, String requestPayload) {
    LoggerContext.addToContext("request-serviceURL", exchange.getRequestURL());
    LoggerContext.addToContext("request-payload", requestPayload);
    logger.info("Incoming request");
  }
}
