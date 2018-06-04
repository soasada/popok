package com.popokis.popok.http.handler;

import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import com.popokis.popok.util.validator.Validator;

public final class DefaultServiceSyncHandler<Req extends Identifiable, Res> extends AbstractServiceSyncHandler<Req, Res> {

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
    super(extractor, PopokLogger.getLogger(loggerName), service);
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
}
