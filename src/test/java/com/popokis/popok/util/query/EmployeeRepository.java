package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;
import com.popokis.popok.data.QueryGenerator;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.query.common.DefaultAllQuery;
import com.popokis.popok.util.query.common.DefaultDeleteQuery;
import com.popokis.popok.util.query.common.DefaultFindQuery;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class EmployeeRepository implements BasicRepository<Employee> {

  private final String tableName;
  private final QueryGenerator queryGenerator;

  public EmployeeRepository() {
    tableName = "employee";
    queryGenerator = new QueryGenerator("e_");
  }

  @Override
  public Query save(Employee employee) {
    return new Query() {
      @Override
      public String query() {
        return "INSERT INTO " + tableName + " (" + queryGenerator.putPrefix("name", "company_id") + ") VALUES(?, ?)";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, employee.name());
          stm.setLong(2, employee.companyId());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query modify(Employee employee) {
    return new Query() {
      @Override
      public String query() {
        return "UPDATE " + tableName + " SET " + queryGenerator.putQuestionMark("name", "company_id") + " WHERE " +
            queryGenerator.putQuestionMark("id");
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, employee.name());
          stm.setLong(2, employee.companyId());
          stm.setLong(3, employee.id());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query find(long id) {
    return new DefaultFindQuery(id, tableName, queryGenerator);
  }

  @Override
  public Query remove(long id) {
    return new DefaultDeleteQuery(id, tableName, queryGenerator);
  }

  @Override
  public Query all() {
    return new DefaultAllQuery(tableName);
  }
}
