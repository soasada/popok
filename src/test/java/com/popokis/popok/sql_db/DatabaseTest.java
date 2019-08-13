package com.popokis.popok.sql_db;

import com.popokis.popok.sql_db.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    Optional<List<Department>> departments = db.executeQuery(new Query() {
      @Override
      public String query() {
        return "SELECT * FROM department LIMIT 100";
      }

      @Override
      public void parameters(PreparedStatement stm) {}
    }, ListMapper.of(Department.builder().build()));

    assertTrue(departments.isPresent());
    assertFalse(departments.get().isEmpty());
    assertEquals(2, departments.get().size());
  }
}