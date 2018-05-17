package com.popokis.popok.util.query;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CompanyEmployeesQuery implements Query {

  private final long companyId;

  public CompanyEmployeesQuery(long companyId) {
    this.companyId = companyId;
  }

  @Override
  public String query() {
    return "SELECT * FROM company " +
        "JOIN employee ON c_id = e_company_id " +
        "WHERE c_id = ?";
  }

  @Override
  public void parameters(PreparedStatement stm) {
    try {
      stm.setLong(1, companyId);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
