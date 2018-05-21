package com.popokis.popok.util.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Company {
  @JsonCreator
  public static Company create(@Nullable @JsonProperty("id") Long id,
                               @JsonProperty("name") String name) {
    return new AutoValue_Company(id, name);
  }

  @Nullable
  @JsonProperty("id")
  public abstract Long id();

  @JsonProperty("name")
  public abstract String name();
}
