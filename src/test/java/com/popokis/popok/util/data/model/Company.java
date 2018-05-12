package com.popokis.popok.util.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@AutoValue
public abstract class Company {
  @JsonCreator
  public static Company create(@Nullable Long id,
                               @JsonProperty("name") String name,
                               @Nullable List<Employee> employees) {
    return new AutoValue_Company(id, name, Objects.nonNull(employees) ? List.of(employees.toArray(new Employee[0])) : null);
  }

  @Nullable
  @JsonProperty("id")
  public abstract Long id();

  @JsonProperty("name")
  public abstract String name();

  @Nullable
  @JsonProperty("employees")
  public abstract List<Employee> employees();
}
