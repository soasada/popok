package com.popokis.popok.sql_db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseTest {

  private static final Database db = Database.create(HikariConnectionPool.getInstance().get());

  @BeforeEach
  void setUp() {
    BootstrapDatabase.setUp();
  }

  @Test
  void shouldExecuteADML() {
    System.out.println("HOLA MUNDO");
  }
}