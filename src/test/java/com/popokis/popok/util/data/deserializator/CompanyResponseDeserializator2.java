package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.CompanyResponse;

import java.util.ArrayList;
import java.util.List;

public final class CompanyResponseDeserializator2 implements Deserializator<CompanyResponse, FixedCachedRowSet> {

  @Override
  public CompanyResponse deserialize(FixedCachedRowSet source) {
    Company company = Company.create(source.getLong("company_id"), source.getString("company_name"));
    Employee firstEmployee = Employee.create(
        source.getLong("employee_id"),
        source.getString("employee_name"),
        source.getLong("employee_company_id"));

    List<Employee> employees = new ArrayList<>();

    while (source.next()) {
      employees.add(Employee.create(
          source.getLong("employee_id"),
          source.getString("employee_name"),
          source.getLong("employee_company_id")));
    }

    employees.add(firstEmployee);
    return CompanyResponse.create(company, employees);
  }
}
