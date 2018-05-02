package com.popokis.popok.http.client;

public interface Client<S> {
  S get(String url);
  S post(String url, String body);
}
