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
                                @JsonProperty("companyId") Long companyId,
                                @Nullable Company company) {
    return new AutoValue_Employee(id, name, companyId, company);
  }

  @Nullable
  @JsonProperty("id")
  public abstract Long id();

  @JsonProperty("name")
  public abstract String name();

  public abstract Long companyId();

  @JsonProperty("company")
  public abstract Company company();
}
