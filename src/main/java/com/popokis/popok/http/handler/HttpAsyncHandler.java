package com.popokis.popok.http.handler;

import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.http.make_request.MakeRequest;
import com.popokis.popok.http.manager.Manager;
import com.popokis.popok.http.manager.PopokExchange;
import com.popokis.popok.http.manager.RequestManager;
import com.popokis.popok.http.manipulator.BasicManipulator;
import com.popokis.popok.http.manipulator.Manipulator;
import com.popokis.popok.runnable.AsyncResponseRunnable;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.Serializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import com.popokis.popok.service.Service;
import com.popokis.popok.service.http.HttpService;
import com.popokis.popok.util.Identificable;
import com.popokis.popok.util.validator.BasicValidator;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.SameThreadExecutor;
import jdk.incubator.http.HttpResponse;

import java.util.concurrent.CompletableFuture;

public final class HttpAsyncHandler<R, S> implements HttpHandler {

  private final String loggerName;
  private final Manager<HttpServerExchange, PopokExchange<CompletableFuture<HttpResponse<String>>>> requestManager;
  private final Manipulator<S> responseManipulator;
  private final Class<S> responseType;

  public HttpAsyncHandler(Extractor extractor,
                          String loggerName,
                          Deserializator<R, String> requestDeserializator,
                          Validator<R> requestValidator,
                          Manipulator<R> requestManipulator,
                          Service<R, CompletableFuture<HttpResponse<String>>> httpAsyncService,
                          Identificable<R> identificable,
                          Manipulator<S> responseManipulator,
                          Class<S> responseType) {
    this.loggerName = loggerName;
    requestManager = new RequestManager<>(
        extractor,
        loggerName,
        requestDeserializator,
        requestValidator,
        requestManipulator,
        httpAsyncService,
        identificable);
    this.responseManipulator = responseManipulator;
    this.responseType = responseType;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    PopokExchange<CompletableFuture<HttpResponse<String>>> popokExchange = requestManager.manage(exchange);

    // This line indicates to Undertow to wait for the async response. We are making async IO in a Worker task thread.
    // If we don't use dispatch, Undertow will reach the end of the handler chain and use the default behaviour: closing
    // the exchange.
    // If you call the dispatch() method the exchange will not end when the call stack returns.
    // Basically this will wait till the call stack returns before running the async task, which means
    // that there is no possibility of a race. Because the exchange is not thread safe this approach makes sure only one
    // thread can run at a time.
    exchange.dispatch(SameThreadExecutor.INSTANCE,
        new AsyncResponseRunnable<>(loggerName, popokExchange, responseType, exchange, responseManipulator));
  }

  public static class Builder<R, S> {

    private final String loggerName;
    private final MakeRequest<CompletableFuture<HttpResponse<String>>> request;
    private final String endpoint;
    private final Class<S> responseType;

    private Extractor extractor = exchange -> "";
    private Deserializator<R, String> requestDeserializator = serialized -> null;
    private Validator<R> requestValidator = new BasicValidator<>();
    private Manipulator<R> requestManipulator = new BasicManipulator<>();
    private Identificable<R> identificable = object -> "";
    private Manipulator<S> responseManipulator = new BasicManipulator<>();
    private Serializator<String, R> requestSerializator = new JacksonSerializator<>();

    public Builder(String loggerName,
                   MakeRequest<CompletableFuture<HttpResponse<String>>> request,
                   String endpoint,
                   Class<S> responseType) {
      this.loggerName = loggerName;
      this.request = request;
      this.endpoint = endpoint;
      this.responseType = responseType;
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

    public Builder<R, S> responseManipulator(Manipulator<S> responseManipulator) {
      this.responseManipulator = responseManipulator;
      return this;
    }

    public Builder<R, S> requestSerializator(Serializator<String, R> requestSerializator) {
      this.requestSerializator = requestSerializator;
      return this;
    }

    public HttpAsyncHandler<R, S> build() {
      return new HttpAsyncHandler<>(
          extractor,
          loggerName,
          requestDeserializator,
          requestValidator,
          requestManipulator,
          new HttpService<>(request, requestSerializator, endpoint),
          identificable,
          responseManipulator,
          responseType);
    }
  }
}
