package com.popokis.popok.util.query;

import com.popokis.popok.data.query.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CompanyEmployeesAliasQuery implements Query {

  private final long companyId;

  public CompanyEmployeesAliasQuery(long companyId) {
    this.companyId = companyId;
  }

  @Override
  public String query() {
    return "SELECT c.c_id AS company_id, c.c_name AS company_name, " +
        "e.e_id AS employee_id, e.e_name AS employee_name, e.e_company_id AS employee_company_id FROM company AS c " +
        "JOIN employee AS e ON c.c_id = e.e_company_id " +
        "WHERE c.c_id = ?";
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
