package com.popokis.popok.http.handler.http;

import com.clickferry.oceanus_lib.http.extractor.Extractor;
import com.clickferry.oceanus_lib.http.manipulator.Manipulator;
import com.clickferry.oceanus_lib.service.Service;
import com.clickferry.oceanus_lib.souk.common.Deserializator;
import com.clickferry.oceanus_lib.souk.common.JacksonSerializator;
import com.clickferry.oceanus_lib.util.Identifiable;
import com.clickferry.oceanus_lib.validate.Validator;
import com.clickferry.oceanus_lib.value_boat.OceanusResponse;
import com.clickferry.yarr.logger.YarrLogger;
import jdk.incubator.http.HttpResponse;

import java.util.concurrent.CompletableFuture;

public final class DefaultHttpAsyncHandler<Req extends Identifiable<Req>, Res> extends AbstractHttpAsyncHandler<Req, Res> {

  private final Deserializator<Req, String> requestDeserializator;
  private final Validator<Req> requestValidator;
  private final Manipulator<Req> requestManipulator;
  private final Manipulator<Res> responseManipulator;
  private final Deserializator<OceanusResponse<Res>, String> responseDeserializator;

  public DefaultHttpAsyncHandler(Extractor extractor,
                                 String loggerName,
                                 Deserializator<Req, String> requestDeserializator,
                                 Validator<Req> requestValidator,
                                 Manipulator<Req> requestManipulator,
                                 Service<Req, CompletableFuture<HttpResponse<String>>> service,
                                 Manipulator<Res> responseManipulator,
                                 Deserializator<OceanusResponse<Res>, String> responseDeserializator) {
    super(extractor, YarrLogger.getLogger(loggerName), service);
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
  protected String serialize(OceanusResponse<Res> oceanusResponse) {
    return new JacksonSerializator<>().serialize(oceanusResponse);
  }

  @Override
  protected OceanusResponse<Res> deserializeResponse(String rawResponse) {
    return responseDeserializator.deserialize(rawResponse);
  }
}
