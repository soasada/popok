package com.popokis.popok.http.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class PopokResponse<S> {
  @JsonCreator
  public static <S> PopokResponse<S> create(@JsonProperty("response") Response response,
                                            @Nullable @JsonProperty("payload") S payload) {
    return new AutoValue_PopokResponse<>(response, payload);
  }

  @JsonProperty("response")
  public abstract Response response();

  @Nullable
  @JsonProperty("payload")
  public abstract S payload();
}
