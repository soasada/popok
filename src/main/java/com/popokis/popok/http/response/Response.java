package com.popokis.popok.http.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Response {
  @JsonCreator
  public static Response create(@JsonProperty("id") String id,
                                @JsonProperty("code") String code,
                                @JsonProperty("message") String message) {

    return new AutoValue_Response(id, code, message);
  }

  @JsonProperty("id")
  public abstract String id();

  @JsonProperty("code")
  public abstract String code();

  @JsonProperty("message")
  public abstract String message();
}