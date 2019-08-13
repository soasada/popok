package com.popokis.popok.sql_db;

public final class BootstrapDatabase {

  private static final Database db = Database.create(HikariConnectionPool.getInstance().get());

  private BootstrapDatabase() {}

  public static void setUp() {
    db.executeDML(QueryFactory.create("DROP TABLE IF EXISTS department"));
    db.executeDML(QueryFactory.create("CREATE TABLE department ("
        + "id INTEGER NOT NULL IDENTITY PRIMARY KEY, "
        + "timestamp TIMESTAMP, "
        + "name VARCHAR(255) NOT NULL"
        + ")"));
    db.executeDML(QueryFactory.create("DROP TABLE IF EXISTS employee"));
    db.executeDML(QueryFactory.create("CREATE TABLE employee ("
        + "id INTEGER NOT NULL IDENTITY PRIMARY KEY, "
        + "name VARCHAR(255) NOT NULL, "
        + "department_id BIGINT, "
        + "FOREIGN KEY (department_id) REFERENCES department(id)"
        + ")"));
  }
}
