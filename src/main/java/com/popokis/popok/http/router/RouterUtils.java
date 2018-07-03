package com.popokis.popok.http.router;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public final class RouterUtils {

  private RouterUtils() {}

  public static HttpHandler loadRoutes(Router router) {
    RoutingHandler tempHandler = new RoutingHandler();

    for (Route route : router.routes()) {
      tempHandler.add(route.method(), route.endpoint(), route.handler());
    }

    return Handlers.routing().addAll(tempHandler);
  }
}
