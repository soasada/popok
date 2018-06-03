package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpServerExchange;

import static com.popokis.popok.http.handler.http.AbstractServiceHandler.OK_CODE;
import static com.popokis.popok.http.handler.http.AbstractServiceHandler.OK_MESSAGE;

public final class DataBaseHandler<Req, Res> extends AbstractHandler<Req, Res> {

  private final Deserializator<Req,String> requestDeserializator;
  private final Validator<Req> requestValidator;

  public DataBaseHandler(Extractor extractor,
                         String loggerName,
                         Deserializator<Req, String> requestDeserializator,
                         Validator<Req> requestValidator,
                         Service<Req, Res> service) {
    super(extractor, PopokLogger.getLogger(loggerName), service);
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
  }

  @Override
  protected Req deserializeRequest(String requestPayload) {
    return requestDeserializator.deserialize(requestPayload);
  }

  @Override
  protected void validate(Req request) {
    requestValidator.validate(request);
  }

  @Override
  protected Req manipulateRequest(Req request) {
    return request;
  }

  @Override
  public final void handleRequest(HttpServerExchange exchange) {
    String requestPayload = extractor.from(exchange);
    String requestId = "DB";

    try {
      Req deserializedRequest = deserializeRequest(requestPayload);
      validate(deserializedRequest);
      Res response = service.call(deserializedRequest);
      RestResponse<Res> oceanusResponse = RestResponse.create(Response.create(requestId, OK_CODE, OK_MESSAGE), response);
      String serializedResponse = new JacksonSerializator<>().serialize(oceanusResponse);
      ResponseSender.asJson(exchange, serializedResponse);
    } catch (Exception e) {
      ResponseSender.asJson(exchange, requestId, e, logger);
    }
  }
}
