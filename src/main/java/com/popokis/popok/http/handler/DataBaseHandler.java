package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataBaseHandler<R, S> implements HttpHandler {

  private final Extractor extractor;
  private final Logger logger;
  private final Service<R, S> service;
  private final Deserializator<R, String> requestDeserializator;
  private final Validator<R> requestValidator;

  public DataBaseHandler(Extractor extractor,
                         String loggerName,
                         Service<R, S> service,
                         Deserializator<R, String> requestDeserializator,
                         Validator<R> requestValidator) {
    this.extractor = extractor;
    this.logger = LoggerFactory.getLogger(loggerName);
    this.service = service;
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
  }

  @Override
  public final void handleRequest(HttpServerExchange exchange) {
    String requestPayload = extractor.from(exchange);
    String requestId = "DB";

    try {
      R deserializedRequest = requestDeserializator.deserialize(requestPayload);
      requestValidator.validate(deserializedRequest);
      S response = service.call(deserializedRequest);
      RestResponse<S> restResponse = RestResponse.create(Response.ok(requestId), response);
      String serializedResponse = new JacksonSerializator<>().serialize(restResponse);
      ResponseSender.asJson(exchange, serializedResponse);
    } catch (Exception e) {
      ResponseSender.asJson(exchange, requestId, e, logger);
    }
  }
}
