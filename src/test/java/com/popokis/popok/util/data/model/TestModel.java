package com.popokis.popok.util.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class TestModel {
  @JsonCreator
  public static TestModel create(@Nullable @JsonProperty("id") Long id,
                                 @JsonProperty("name") String name) {
    return new AutoValue_TestModel(id, name);
  }

  @JsonProperty("id")
  public abstract Long id();

  @Nullable
  @JsonProperty("name")
  public abstract String name();
}
