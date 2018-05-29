package com.popokis.popok.util.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.EmployeeResponse;

public final class EmployeeResponseMapper implements Mapper<EmployeeResponse> {

  private final Mapper<Company> companyMapper;
  private final Mapper<Employee> employeeMapper;

  public EmployeeResponseMapper() {
    companyMapper = new CompanyMapper();
    employeeMapper = new EmployeeMapper();
  }

  @Override
  public EmployeeResponse map(FixedCachedRowSet source) {
    Employee employee = employeeMapper.map(source);
    Company company = companyMapper.map(source);
    return EmployeeResponse.create(employee, company);
  }
}
