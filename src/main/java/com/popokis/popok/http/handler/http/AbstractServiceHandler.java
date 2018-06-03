package com.popokis.popok.http.handler.http;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.handler.AbstractHandler;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;

public abstract class AbstractServiceHandler<Req extends Identifiable, Res, MidRes> extends AbstractHandler<Req, Res> {

  public static final String OK_CODE = "0";
  public static final String OK_MESSAGE = "OK";

  private Req request;

  public AbstractServiceHandler(Extractor extractor, Logger logger, Service<Req, Res> service) {
    super(extractor, logger, service);
  }

  protected abstract MidRes manipulateResponse(MidRes response);

  protected abstract String serialize(RestResponse<MidRes> oceanusResponse);

  protected abstract void finalizeResponse(Res response, HttpServerExchange exchange);

  protected final Res manageRequest(HttpServerExchange exchange) {
    String payload = extractor.from(exchange);
    logRequest(exchange, payload);
    String requestId = "-1";

    try {
      Req deserializedRequest = deserializeRequest(payload);
      requestId = deserializedRequest.id();
      validate(deserializedRequest);
      request = manipulateRequest(deserializedRequest);
      return service.call(request);
    } catch (Exception e) {
      ResponseSender.asJson(exchange, requestId, e, logger);
      throw new RuntimeException(e);
    }
  }

  protected final String manageResponse(MidRes response, HttpServerExchange exchange) {
    try {
      MidRes manipulatedResponse = manipulateResponse(response);
      Response responseWithCode = Response.create(request().id(), OK_CODE, OK_MESSAGE);
      RestResponse<MidRes> restResponse = RestResponse.create(responseWithCode, manipulatedResponse);
      String stringResponse = serialize(restResponse);
      logResponse(stringResponse);
      return stringResponse;
    } catch (Exception e) {
      ResponseSender.asJson(exchange, request().id(), e, logger);
      throw new RuntimeException(e);
    }
  }

  protected final Req request() {
    return request;
  }
}
