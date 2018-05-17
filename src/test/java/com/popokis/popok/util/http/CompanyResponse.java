package com.popokis.popok.util.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;

import java.util.List;

@AutoValue
public abstract class CompanyResponse {
  @JsonCreator
  public static CompanyResponse create(Company company, List<Employee> employees) {
    return new AutoValue_CompanyResponse(company, List.of(employees.toArray(new Employee[0])));
  }

  @JsonProperty("company")
  public abstract Company company();

  @JsonProperty("employees")
  public abstract List<Employee> employees();
}
