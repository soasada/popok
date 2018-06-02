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

public final class DefaultServiceSyncHandler<Req extends Identifiable<Req>, Res> extends AbstractServiceSyncHandler<Req, Res> {

  private final Deserializator<Req, String> requestDeserializator;
  private final Validator<Req> requestValidator;
  private final Manipulator<Req> requestManipulator;
  private final Manipulator<Res> responseManipulator;

  public DefaultServiceSyncHandler(Extractor extractor,
                                   String loggerName,
                                   Deserializator<Req, String> requestDeserializator,
                                   Validator<Req> requestValidator,
                                   Manipulator<Req> requestManipulator,
                                   Service<Req, Res> service,
                                   Manipulator<Res> responseManipulator) {
    super(extractor, YarrLogger.getLogger(loggerName), service);
    this.requestDeserializator = requestDeserializator;
    this.requestValidator = requestValidator;
    this.requestManipulator = requestManipulator;
    this.responseManipulator = responseManipulator;
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
}
