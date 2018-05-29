package com.popokis.popok.util.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.ListMapper;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.CompanyResponse;

import java.util.List;

public final class CompanyResponseMapper implements Mapper<CompanyResponse> {

  private final Mapper<Company> companyMapper;
  private final Mapper<Employee> employeeMapper;
  private final Mapper<List<Employee>> employeesMapper;

  public CompanyResponseMapper() {
    companyMapper = new CompanyMapper();
    employeeMapper = new EmployeeMapper();
    employeesMapper = new ListMapper<>(employeeMapper);
  }

  @Override
  public CompanyResponse map(FixedCachedRowSet source) {
    Company company = companyMapper.map(source);
    Employee firstEmployee = employeeMapper.map(source);
    List<Employee> employees = employeesMapper.map(source);
    employees.add(firstEmployee);
    return CompanyResponse.create(company, employees);
  }
}
