package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.db.ListDeserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.CompanyResponse;

import java.util.List;

public final class CompanyResponseDeserializator implements Deserializator<CompanyResponse, FixedCachedRowSet> {

  private final Deserializator<Company, FixedCachedRowSet> companyDeserializator;
  private final Deserializator<Employee, FixedCachedRowSet> employeeDeserializator;
  private final Deserializator<List<Employee>, FixedCachedRowSet> employeesDeserializator;

  public CompanyResponseDeserializator() {
    companyDeserializator = new CompanyDeserializator();
    employeeDeserializator = new EmployeeDeserializator();
    employeesDeserializator = new ListDeserializator<>(employeeDeserializator);
  }

  @Override
  public CompanyResponse deserialize(FixedCachedRowSet source) {
    Company company = companyDeserializator.deserialize(source);
    Employee firstEmployee = employeeDeserializator.deserialize(source);
    List<Employee> employees = employeesDeserializator.deserialize(source);
    employees.add(firstEmployee);
    return CompanyResponse.create(company, employees);
  }
}
