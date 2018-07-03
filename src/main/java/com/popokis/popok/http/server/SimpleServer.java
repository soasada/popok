package com.popokis.popok.http.server;

import com.popokis.popok.http.router.Router;
import com.popokis.popok.http.router.RouterUtils;
import io.undertow.Undertow;

public final class SimpleServer implements PopokHttpServer {

  private final int port;
  private final String host;
  private final Router router;
  private final Undertow server;

  public SimpleServer(int port, String host, Router router) {
    this.port = port;
    this.host = host;
    this.router = router;

    server = Undertow.builder()
        .addHttpListener(port, host, RouterUtils.loadRoutes(router))
        .build();
  }

  @Override
  public void start() {
    server.start();
  }

  @Override
  public void stop() {
    server.stop();
  }

  @Override
  public String url() {
    return "http://" + host + ":" + port + router.version();
  }
}
