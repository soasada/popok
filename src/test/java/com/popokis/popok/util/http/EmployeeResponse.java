package com.popokis.popok.util.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;

@AutoValue
public abstract class EmployeeResponse {
  @JsonCreator
  public static EmployeeResponse create(Employee employee, Company company) {
    return new AutoValue_EmployeeResponse(employee, company);
  }

  @JsonProperty("employee")
  public abstract Employee employee();

  @JsonProperty("company")
  public abstract Company company();
}
