package com.popokis.popok.util.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.time.LocalDateTime;

@AutoValue
public abstract class FakeResponse {
  @JsonCreator
  public static FakeResponse create(@JsonProperty("id") int id,
                                    @JsonProperty("code") String code,
                                    @JsonProperty("timestamp") LocalDateTime timestamp) {
    return new AutoValue_FakeResponse(id, code, timestamp);
  }

  @JsonProperty("id")
  public abstract int id();

  @JsonProperty("code")
  public abstract String code();

  @JsonProperty("timestamp")
  public abstract LocalDateTime timestamp();
}
