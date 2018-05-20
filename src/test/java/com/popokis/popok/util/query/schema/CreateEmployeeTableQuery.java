package com.popokis.popok.util.query.schema;

import com.popokis.popok.data.query.Query;

import java.sql.PreparedStatement;

public final class CreateEmployeeTableQuery implements Query {

  @Override
  public String query() {
    return "CREATE TABLE IF NOT EXISTS employee (e_id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
        "e_name VARCHAR(255) NOT NULL, " +
        "e_company_id INT UNSIGNED NOT NULL, " +
        "PRIMARY KEY (e_id), " +
        "INDEX fk_employee_company_idx (e_company_id ASC), " +
        "CONSTRAINT fk_employee_company " +
        "FOREIGN KEY (e_company_id) " +
        "REFERENCES company (c_id) " +
        "ON DELETE NO ACTION " +
        "ON UPDATE NO ACTION)";
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
