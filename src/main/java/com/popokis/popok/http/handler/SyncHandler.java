package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.manager.Manager;
import com.popokis.popok.http.manager.PopokExchange;
import com.popokis.popok.http.manager.RequestManager;
import com.popokis.popok.http.manager.ResponseManager;
import com.popokis.popok.http.manipulator.BasicManipulator;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.log.PopokLogger;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identificable;
import com.popokis.popok.util.validator.BasicValidator;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public final class SyncHandler<R, S> implements HttpHandler {

  private final String loggerName;
  private final Manager<HttpServerExchange, PopokExchange<S>> requestManager;
  private final Manipulator<S> responseManipulator;

  private SyncHandler(Extractor extractor,
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

  public static class Builder<R, S> {

    private final String loggerName;
    private final Service<R, S> service;

    private Extractor extractor = exchange -> "";
    private Deserializator<R, String> requestDeserializator = serialized -> null;
    private Validator<R> requestValidator = new BasicValidator<>();
    private Manipulator<R> requestManipulator = new BasicManipulator<>();
    private Identificable<R> identificable = object -> "";
    private Manipulator<S> responseManipulator = new BasicManipulator<>();

    public Builder(String loggerName, Service<R, S> service) {
      this.loggerName = loggerName;
      this.service = service;
    }

    public Builder<R, S> extractor(Extractor extractor) {
      this.extractor = extractor;
      return this;
    }

    public Builder<R, S> requestDeserializator(Deserializator<R, String> deserializator) {
      this.requestDeserializator = deserializator;
      return this;
    }

    public Builder<R, S> requestValidator(Validator<R> validator) {
      this.requestValidator = validator;
      return this;
    }

    public Builder<R, S> requestManipulator(Manipulator<R> manipulator) {
      this.requestManipulator = manipulator;
      return this;
    }

    public Builder<R, S> identificable(Identificable<R> identificable) {
      this.identificable = identificable;
      return this;
    }

    public Builder<R, S>  responseManipulator(Manipulator<S> responseManipulator) {
      this.responseManipulator = responseManipulator;
      return this;
    }

    public SyncHandler<R, S> build() {
      return new SyncHandler<>(
          extractor,
          loggerName,
          requestDeserializator,
          requestValidator,
          requestManipulator,
          service,
          identificable,
          responseManipulator);
    }
  }
}
