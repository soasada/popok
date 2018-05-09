package com.popokis.popok.util.query.schema;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;

public final class CreateEmployeeTableQuery implements Query {

  @Override
  public String query() {
    return "CREATE TABLE IF NOT EXISTS employee (id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
        "name VARCHAR(255) NOT NULL, " +
        "company_id INT UNSIGNED NOT NULL, " +
        "PRIMARY KEY (id), " +
        "INDEX fk_employee_company_idx (company_id ASC), " +
        "CONSTRAINT fk_employee_company " +
        "FOREIGN KEY (company_id) " +
        "REFERENCES company (id) " +
        "ON DELETE NO ACTION " +
        "ON UPDATE NO ACTION)";
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
