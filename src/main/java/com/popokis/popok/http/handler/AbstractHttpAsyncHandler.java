package com.popokis.popok.http.handler;

import com.popokis.popok.http.ResponseSender;
import com.popokis.popok.http.extractor.Extractor;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.Identifiable;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.SameThreadExecutor;
import jdk.incubator.http.HttpResponse;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractHttpAsyncHandler<Req extends Identifiable, Res>
    extends AbstractServiceHandler<Req, CompletableFuture<HttpResponse<String>>, Res> {

  public AbstractHttpAsyncHandler(Extractor extractor,
                                  Logger logger,
                                  Service<Req, CompletableFuture<HttpResponse<String>>> service) {
    super(extractor, logger, service);
  }

  protected String manageHttpResponse(HttpResponse<String> stringHttpResponse) {
    return stringHttpResponse.body();
  }

  protected abstract Res responseMiddleware(String rawResponse, HttpServerExchange exchange);

  @Override
  protected final void finalizeResponse(CompletableFuture<HttpResponse<String>> response, HttpServerExchange exchange) {
    response
        .thenApply(this::manageHttpResponse)
        .thenApply(rawResponse -> manageResponse(responseMiddleware(rawResponse, exchange), exchange))
        .whenComplete((stringResponse, exception) -> {
          if (Objects.nonNull(stringResponse)) {
            ResponseSender.asJson(exchange, stringResponse);
          } else {
            ResponseSender.asJson(exchange, request().id(), exception, logger);
          }
        });
  }

  @Override
  public final void handleRequest(HttpServerExchange exchange) {
    CompletableFuture<HttpResponse<String>> future = manageRequest(exchange);

    // This line indicates to Undertow to wait for the async response. We are making async IO in a Worker task thread.
    // If we don't use dispatch, Undertow will reach the end of the handler chain and use the default behaviour: closing
    // the exchange.
    // If you call the dispatch() method the exchange will not end when the call stack returns.
    // Basically this will wait till the call stack returns before running the async task, which means
    // that there is no possibility of a race. Because the exchange is not thread safe this approach makes sure only one
    // thread can run at a time.
    exchange.dispatch(SameThreadExecutor.INSTANCE, () -> finalizeResponse(future, exchange));
  }
}