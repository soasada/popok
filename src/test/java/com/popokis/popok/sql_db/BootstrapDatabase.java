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

package com.popokis.popok.sql_db;

public final class BootstrapDatabase {

  private static final Database db = Database.create(HikariConnectionPool.getInstance().get());

  private BootstrapDatabase() {}

  public static void setUp() {
    db.executeDML(QueryFactory.create("DROP TABLE IF EXISTS employee"));
    db.executeDML(QueryFactory.create("DROP TABLE IF EXISTS department"));
    db.executeDML(QueryFactory.create("CREATE TABLE department ("
        + "id INTEGER NOT NULL IDENTITY PRIMARY KEY, "
        + "timestamp TIMESTAMP, "
        + "name VARCHAR(255) NOT NULL"
        + ")"));
    db.executeDML(QueryFactory.create("CREATE TABLE employee ("
        + "id INTEGER NOT NULL IDENTITY PRIMARY KEY, "
        + "name VARCHAR(255) NOT NULL, "
        + "department_id BIGINT, "
        + "FOREIGN KEY (department_id) REFERENCES department(id)"
        + ")"));

    db.executeInsert(QueryFactory.create("INSERT INTO department (timestamp, name) VALUES (NOW(), 'department1')"));
    db.executeInsert(QueryFactory.create("INSERT INTO department (timestamp, name) VALUES (NOW(), 'department2')"));
    db.executeInsert(QueryFactory.create("INSERT INTO employee (name, department_id) VALUES ('Thomas', 1)"));
    db.executeInsert(QueryFactory.create("INSERT INTO employee (name, department_id) VALUES ('Maria', 1)"));
    db.executeInsert(QueryFactory.create("INSERT INTO employee (name, department_id) VALUES ('Johnny', 2)"));
    db.executeInsert(QueryFactory.create("INSERT INTO employee (name, department_id) VALUES ('Carl', 2)"));
  }
}
