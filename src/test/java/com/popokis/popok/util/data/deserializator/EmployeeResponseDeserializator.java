package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.EmployeeResponse;

public final class EmployeeResponseDeserializator implements Deserializator<EmployeeResponse, FixedCachedRowSet> {

  private final Deserializator<Company, FixedCachedRowSet> companyDeserializator;
  private final Deserializator<Employee, FixedCachedRowSet> employeeDeserializator;

  public EmployeeResponseDeserializator() {
    companyDeserializator = new CompanyDeserializator();
    employeeDeserializator = new EmployeeDeserializator();
  }

  @Override
  public EmployeeResponse deserialize(FixedCachedRowSet source) {
    Employee employee = employeeDeserializator.deserialize(source);
    Company company = companyDeserializator.deserialize(source);
    return EmployeeResponse.create(employee, company);
  }
}
