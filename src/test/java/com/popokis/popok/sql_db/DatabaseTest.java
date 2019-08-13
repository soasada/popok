package com.popokis.popok.sql_db;

import com.popokis.popok.sql_db.mapper.DepartmentEmployeesMapper;
import com.popokis.popok.sql_db.model.Department;
import com.popokis.popok.sql_db.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

  private static final Database db = Database.create(HikariConnectionPool.getInstance().get());

  @BeforeEach
  void setUp() {
    BootstrapDatabase.setUp();
  }

  @Test
  void shouldReturnsTwoDepartments() {
    Optional<List<Department>> departments = db.executeQuery(
        QueryFactory.create("SELECT * FROM department LIMIT 100"),
        ListMapper.of(Department.builder().build())
    );

    assertTrue(departments.isPresent());
    assertFalse(departments.get().isEmpty());
    assertEquals(2, departments.get().size());
  }

  @Test
  void shouldReturnsFourEmployees() {
    Optional<List<Employee>> employees = db.executeQuery(
        QueryFactory.create("SELECT * FROM employee LIMIT 100"),
        ListMapper.of(Employee.builder().build())
    );

    assertTrue(employees.isPresent());
    assertFalse(employees.get().isEmpty());
    assertEquals(4, employees.get().size());
  }

  @Test
  void shouldExecuteTenThousandQueries() throws ExecutionException, InterruptedException {
    List<Future<Optional<List<Employee>>>> results = new ArrayList<>();
    List<Optional<List<Employee>>> finalResult = new ArrayList<>();

    for (int i = 0; i < 10_000; i++) {
      results.add(ForkJoinPool.commonPool().submit(() -> db.executeQuery(
          QueryFactory.create("SELECT * FROM employee LIMIT 100"),
          ListMapper.of(Employee.builder().build())
      )));
    }

    for (Future<Optional<List<Employee>>> futureRoundResult : results) {
      finalResult.add(futureRoundResult.get());
    }

    assertEquals(10_000, finalResult.size());
  }

  @Test
  void shouldExecuteAndMapAJoinWithAliases() {
    Query join = QueryFactory.create("SELECT department.*, employee.id AS employee_id, employee.name AS employee_name, "
        + "employee.department_id AS employee_department_id FROM department "
        + "LEFT JOIN employee ON department.id = employee.department_id");

    Optional<Set<Department>> departments = db.executeQuery(join, new DepartmentEmployeesMapper());

    assertTrue(departments.isPresent());
    assertEquals(2, departments.get().size());
    assertEquals(2, new ArrayList<>(departments.get()).get(0).getEmployees().size());
    assertEquals(2, new ArrayList<>(departments.get()).get(1).getEmployees().size());
  }
}