package com.popokis.popok.http.handler;

import com.clickferry.oceanus_lib.exception.OceanusRuntimeException;
import com.clickferry.oceanus_lib.http.ServeResponse;
import com.clickferry.oceanus_lib.http.extractor.Extractor;
import com.clickferry.oceanus_lib.service.Service;
import com.clickferry.oceanus_lib.souk.common.Deserializator;
import com.clickferry.oceanus_lib.souk.common.JacksonSerializator;
import com.clickferry.oceanus_lib.validate.Validator;
import com.clickferry.oceanus_lib.value_boat.OceanusResponse;
import com.clickferry.oceanus_lib.value_boat.entities.Response;
import com.clickferry.oceanus_lib.value_boat.exception.ErrorCode;
import com.clickferry.yarr.logger.YarrLogger;
import io.undertow.server.HttpServerExchange;

public final class DataBaseHandler<Req, Res> extends AbstractHandler<Req, Res> {

  private final Deserializator<Req,String> requestDeserializator;
  private final Validator<Req> requestValidator;

  public DataBaseHandler(Extractor extractor,
                         String loggerName,
                         Deserializator<Req, String> requestDeserializator,
                         Validator<Req> requestValidator,
                         Service<Req, Res> service) {
    super(extractor, YarrLogger.getLogger(loggerName), service);
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
    String requestPayload = extractor.payloadFrom(exchange);
    String requestId = "DB";

    try {
      Req deserializedRequest = deserializeRequest(requestPayload);
      validate(deserializedRequest);
      Res response = service.call(deserializedRequest);
      OceanusResponse<Res> oceanusResponse =
          OceanusResponse.create(Response.create(requestId, ErrorCode.A0, ErrorCode.A0.message()), response);
      String serializedResponse = new JacksonSerializator<>().serialize(oceanusResponse);
      ServeResponse.asJson(exchange, serializedResponse);
    } catch (OceanusRuntimeException e) {
      ServeResponse.asJson(exchange, requestId, e, logger);
    } catch (Exception e) {
      ServeResponse.asJson(exchange, requestId, e, logger);
    }
  }
}
