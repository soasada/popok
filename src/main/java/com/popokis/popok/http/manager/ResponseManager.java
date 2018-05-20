package com.popokis.popok.http.manager;

import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.log.context.LoggerContext;
import com.popokis.popok.serialization.Serializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import org.slf4j.Logger;

public final class ResponseManager<S> implements Manager<S, String> {

  public static final String OK_CODE = "0";
  public static final String OK_MESSAGE = "OK";

  private final Manipulator<S> responseManipulator;
  private final Serializator<String, RestResponse<S>> responseSerializator;
  private final String requestId;
  private final Logger logger;

  public ResponseManager(Manipulator<S> responseManipulator, String requestId, String loggerName) {
    this.responseManipulator = responseManipulator;
    responseSerializator = new JacksonSerializator<>();
    this.requestId = requestId;
    logger = PopokLogger.getLogger(loggerName);
  }

  @Override
  public String manage(S response) {
    S manipulatedResponse = responseManipulator.manipulate(response);
    RestResponse<S> restResponse = RestResponse.create(Response.create(requestId, OK_CODE, OK_MESSAGE), manipulatedResponse);
    String stringResponse = responseSerializator.serialize(restResponse);
    logResponse(stringResponse);
    return stringResponse;
  }

  private void logResponse(String response) {
    LoggerContext.addToContext("response", response);
    logger.info("Response");
    LoggerContext.clearContext();
  }
}
