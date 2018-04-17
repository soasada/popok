package com.popokis.popok.http;

public interface Controller<T> {
  void handleRequest(T request);
}
