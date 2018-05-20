package com.popokis.popok.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.popokis.popok.http.response.Response;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.log.context.LoggerContext;
import com.popokis.popok.serialization.json.CustomObjectMapper;
import org.slf4j.Logger;

public final class ExceptionUtils {

  private ExceptionUtils() {
  }

  public static Throwable getRootCause(Throwable throwable) {
    if (throwable.getCause() != null) {
      return getRootCause(throwable.getCause());
    }

    return throwable;
  }

  public static void logException(Throwable e, String requestId, Logger logger) {
    LoggerContext.addToContext("request-id", requestId);
    logger.error("Exception", e);
    LoggerContext.clearContext();
  }

  public static String errorResponse(String requestId, String code, String message) {
    try {
      RestResponse response = RestResponse.create(Response.create(requestId, code, message == null ? "" : message), null);
      return CustomObjectMapper.getInstance().mapper().writeValueAsString(response);
    } catch (JsonProcessingException e) {
      return "Fatal Error: " + e.getMessage();
    }
  }
}
