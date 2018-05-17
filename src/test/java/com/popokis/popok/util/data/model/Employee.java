package com.popokis.popok.util.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Employee {
  @JsonCreator
  public static Employee create(@Nullable Long id,
                                @JsonProperty("name") String name,
                                @JsonProperty("companyId") Long companyId) {
    return new AutoValue_Employee(id, name, companyId);
  }

  @Nullable
  @JsonProperty("id")
  public abstract Long id();

  @JsonProperty("name")
  public abstract String name();

  @JsonProperty("companyId")
  public abstract Long companyId();
}
