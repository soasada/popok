package com.popokis.popok.util.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.model.Employee;

public final class EmployeeMapper implements Mapper<Employee> {
  @Override
  public Employee map(FixedCachedRowSet source) {
    return Employee.create(
        source.getLong("e_id"),
        source.getString("e_name"),
        source.getLong("e_company_id"));
  }
}
