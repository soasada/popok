/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popokis.popok.sql_db.mapper;

import com.popokis.popok.sql_db.JdbcMapper;
import com.popokis.popok.sql_db.model.Department;
import com.popokis.popok.sql_db.model.Employee;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class DepartmentEmployeesMapper implements JdbcMapper<Set<Department>> {

  private final JdbcMapper<Department> departmentMapper;

  public DepartmentEmployeesMapper() {
    this.departmentMapper = Department.builder().build();
  }

  @Override
  public Optional<Set<Department>> map(ResultSetWrappingSqlRowSet resultSet) {
    JdbcMapper<Employee> employeeWithAliasMapper = new EmployeeWithAliasMapper();
    Set<Department> departments = new HashSet<>();
    Set<Employee> employees = new HashSet<>();

    do {
      Optional<Department> optionalDepartment = departmentMapper.map(resultSet);
      optionalDepartment.ifPresent(departments::add);
      Optional<Employee> optionalEmployee = employeeWithAliasMapper.map(resultSet);
      optionalEmployee.ifPresent(employees::add);
    } while (resultSet.next());

    Set<Department> realDepartments = new HashSet<>();

    for (Department department : departments) {
      Set<Employee> employeeSet = new HashSet<>();
      for (Employee employee : employees) {
        if (department.getId().equals(employee.getDepartmentId())) employeeSet.add(employee);
      }
      realDepartments.add(department.toBuilder().employees(employeeSet).build());
    }

    return Optional.of(realDepartments);
  }

  private static class EmployeeWithAliasMapper implements JdbcMapper<Employee> {

    @Override
    public Optional<Employee> map(ResultSetWrappingSqlRowSet resultSet) {
      if (resultSet.getLong("employee_id") == 0) return Optional.empty();

      return Optional.of(
          Employee.create(
              resultSet.getLong("employee_id"),
              resultSet.getString("employee_name"),
              resultSet.getLong("employee_department_id")
          )
      );
    }
  }
}
