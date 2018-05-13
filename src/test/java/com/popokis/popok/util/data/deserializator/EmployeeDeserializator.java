package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.model.Employee;

public final class EmployeeDeserializator implements Deserializator<Employee, FixedCachedRowSet> {
  @Override
  public Employee deserialize(FixedCachedRowSet source) {
    return Employee.create(
        source.getLong("e_id"),
        source.getString("e_name"),
        source.getLong("e_company_id"));
  }
}
