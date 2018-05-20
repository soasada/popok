package com.popokis.popok.http.manager;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PopokExchange<S> {

  public static <T> PopokExchange<T> create(String id, T response) {
    return new AutoValue_PopokExchange<>(id, response);
  }

  public abstract String id();

  public abstract S response();
}