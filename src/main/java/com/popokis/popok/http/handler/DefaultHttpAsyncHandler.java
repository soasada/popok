package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpServerExchange;
import jdk.incubator.http.HttpResponse;

import java.util.concurrent.CompletableFuture;

public final class DefaultHttpAsyncHandler<Req extends Identifiable, Res> extends AbstractHttpAsyncHandler<Req, Res> {

  private final Deserializator<Req, String> requestDeserializator;
  private final Validator<Req> requestValidator;
  private final Manipulator<Req> requestManipulator;
  private final Manipulator<Res> responseManipulator;
  private final Deserializator<RestResponse<Res>, String> responseDeserializator;

  public DefaultHttpAsyncHandler(Extractor extractor,
                                 String loggerName,
                                 Deserializator<Req, String> requestDeserializator,
                                 Validator<Req> requestValidator,
                                 Manipulator<Req> requestManipulator,
                                 Service<Req, CompletableFuture<HttpResponse<String>>> service,
                                 Manipulator<Res> responseManipulator,
                                 Deserializator<RestResponse<Res>, String> responseDeserializator) {
    super(extractor, PopokLogger.getLogger(loggerName), service);
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
    this.requestManipulator = requestManipulator;
    this.responseManipulator = responseManipulator;
    this.responseDeserializator = responseDeserializator;
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
    return requestManipulator.manipulate(request);
  }

  @Override
  protected Res manipulateResponse(Res response) {
    return responseManipulator.manipulate(response);
  }

  @Override
  protected Res responseMiddleware(String rawResponse, HttpServerExchange exchange) {
    RestResponse<Res> restResponse = responseDeserializator.deserialize(rawResponse);

    if (!restResponse.response().code().equals(OK_CODE)) {
      ResponseSender.asJson(exchange, request().id(), restResponse);
    }

    return restResponse.payload();
  }
}
