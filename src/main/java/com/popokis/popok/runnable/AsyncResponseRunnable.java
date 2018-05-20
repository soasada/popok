package com.popokis.popok.runnable;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.manager.Manager;
import com.popokis.popok.http.manager.PopokExchange;
import com.popokis.popok.http.manager.ResponseManager;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.http.RestResponseDeserializator;
import io.undertow.server.HttpServerExchange;
import jdk.incubator.http.HttpResponse;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class AsyncResponseRunnable<S> implements Runnable {

  private final String loggerName;
  private final PopokExchange<CompletableFuture<HttpResponse<String>>> popokExchange;
  private final Class<S> responseType;
  private final HttpServerExchange exchange;
  private final Manipulator<S> responseManipulator;

  public AsyncResponseRunnable(String loggerName,
                               PopokExchange<CompletableFuture<HttpResponse<String>>> popokExchange,
                               Class<S> responseType,
                               HttpServerExchange exchange,
                               Manipulator<S> responseManipulator) {
    this.loggerName = loggerName;
    this.popokExchange = popokExchange;
    this.responseType = responseType;
    this.exchange = exchange;
    this.responseManipulator = responseManipulator;
  }

  @Override
  public void run() {
    popokExchange.response()
        .thenApply(HttpResponse::body)
        .thenApply(rawResponse -> {
          Deserializator<RestResponse<S>, String> deserializator = new RestResponseDeserializator<>(responseType);
          RestResponse<S> response = deserializator.deserialize(rawResponse);

          if (!response.response().code().equals(ResponseManager.OK_CODE)) {
            ResponseSender.asJson(exchange, popokExchange.id(), response);
          }

          Manager<S, String> responseManager = new ResponseManager<>(responseManipulator, popokExchange.id(), loggerName);
          return responseManager.manage(response.payload());
        })
        .whenComplete((stringResponse, exception) -> {
          if (Objects.nonNull(stringResponse)) {
            ResponseSender.asJson(exchange, stringResponse);
          } else {
            ResponseSender.asJson(exchange, popokExchange.id(), exception, PopokLogger.getLogger(loggerName));
          }
        });
  }
}
