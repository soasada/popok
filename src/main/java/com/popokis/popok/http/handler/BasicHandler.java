package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.Objects;

public final class BasicHandler<R extends Identifiable, S> implements HttpHandler {

  private final Extractor extractor;
  private final Logger logger;
  private final Service<R, S> service;
  private final Deserializator<R, String> requestDeserializator;
  private final Validator<R> requestValidator;
  private final Manipulator<R> requestManipulator;
  private final Manipulator<S> responseManipulator;

  public BasicHandler(Extractor extractor,
                      Logger logger,
                      Service<R, S> service,
                      Deserializator<R, String> requestDeserializator,
                      Validator<R> requestValidator,
                      Manipulator<R> requestManipulator,
                      Manipulator<S> responseManipulator) {
    this.extractor = extractor;
    this.logger = logger;
    this.service = service;
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
    this.requestManipulator = requestManipulator;
    this.responseManipulator = responseManipulator;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    String payload = extractor.from(exchange);
    if (Objects.nonNull(logger)) logRequest(exchange, payload);
    String requestId = "-1";

    try {
      R deserializedRequest = requestDeserializator.deserialize(payload);
      requestId = deserializedRequest.id();
      requestValidator.validate(deserializedRequest);
      R manipulatedRequest = requestManipulator.manipulate(deserializedRequest);
      S serviceResponse = service.call(manipulatedRequest);
      S manipulatedResponse = responseManipulator.manipulate(serviceResponse);
      RestResponse<S> restResponse = RestResponse.create(Response.ok(requestId), manipulatedResponse);
      String stringResponse = new JacksonSerializator<>().serialize(restResponse);
      if (Objects.nonNull(logger)) logResponse(stringResponse);
      ResponseSender.asJson(exchange, stringResponse);
    } catch (Exception e) {
      ResponseSender.asJson(exchange, requestId, e, logger);
      throw new RuntimeException(e);
    }
  }

  private void logRequest(HttpServerExchange exchange, String requestPayload) {
    MDC.put("request-serviceURL", exchange.getRequestURL());
    logger.info(requestPayload);
  }

  private void logResponse(String response) {
    logger.info(response);
    MDC.clear();
  }
}
