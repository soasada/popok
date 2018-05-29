package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manager.Manager;
import com.popokis.popok.http.manager.PopokExchange;
import com.popokis.popok.http.manager.RequestManager;
import com.popokis.popok.http.manager.ResponseManager;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identificable;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public final class SyncHandler<R, S> implements HttpHandler {

  private final String loggerName;
  private final Manager<HttpServerExchange, PopokExchange<S>> requestManager;
  private final Manipulator<S> responseManipulator;

  public SyncHandler(Extractor extractor,
                      String loggerName,
                      Deserializator<R, String> requestDeserializator,
                      Validator<R> requestValidator,
                      Manipulator<R> requestManipulator,
                      Service<R, S> service,
                      Identificable<R> identificable,
                      Manipulator<S> responseManipulator) {
    this.loggerName = loggerName;
    requestManager = new RequestManager<>(
        extractor,
        loggerName,
        requestDeserializator,
        requestValidator,
        requestManipulator,
        service,
        identificable
    );
    this.responseManipulator = responseManipulator;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    PopokExchange<S> popokExchange = requestManager.manage(exchange);

    try {
      Manager<S, String> responseManager = new ResponseManager<>(responseManipulator, popokExchange.id(), loggerName);
      ResponseSender.asJson(exchange, responseManager.manage(popokExchange.response()));
    } catch (Exception e) {
      ResponseSender.asJson(exchange, popokExchange.id(), e, PopokLogger.getLogger(loggerName));
    }
  }
}
