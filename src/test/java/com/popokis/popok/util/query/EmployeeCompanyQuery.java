package com.popokis.popok.util.query;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class EmployeeCompanyQuery implements Query {

  private final long employeeId;

  public EmployeeCompanyQuery(long employeeId) {
    this.employeeId = employeeId;
  }

  @Override
  public String query() {
    return "SELECT * FROM company " +
        "JOIN employee ON c_id = e_company_id " +
        "WHERE e_id = ?";
  }

  @Override
  public void parameters(PreparedStatement stm) {
    try {
      stm.setLong(1, employeeId);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
