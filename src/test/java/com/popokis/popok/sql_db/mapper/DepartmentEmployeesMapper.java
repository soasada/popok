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
