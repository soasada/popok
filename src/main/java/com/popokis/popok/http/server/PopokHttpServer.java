package com.popokis.popok.http.server;

public interface PopokHttpServer {
  void start();
  void stop();
  String url();
}
