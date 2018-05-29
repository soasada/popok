package com.popokis.popok.util.query;

import com.popokis.popok.data.query.AllQueryFactory;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.data.query.DefaultAllQueryFactory;
import com.popokis.popok.data.query.DefaultDeleteQueryFactory;
import com.popokis.popok.data.query.DefaultFindQueryFactory;
import com.popokis.popok.data.query.DeleteQueryFactory;
import com.popokis.popok.data.query.FindQueryFactory;
import com.popokis.popok.data.query.InsertQueryFactory;
import com.popokis.popok.data.query.Query;
import com.popokis.popok.data.query.QueryGenerator;
import com.popokis.popok.data.query.UpdateQueryFactory;
import com.popokis.popok.util.data.model.Employee;

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
  public InsertQueryFactory<Employee> saveQuery() {
    return employee -> new Query() {
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
  public UpdateQueryFactory<Employee> modifyQuery() {
    return employee -> new Query() {
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
  public FindQueryFactory findQuery() {
    return new DefaultFindQueryFactory(tableName, queryGenerator);
  }

  @Override
  public DeleteQueryFactory removeQuery() {
    return new DefaultDeleteQueryFactory(tableName, queryGenerator);
  }

  @Override
  public AllQueryFactory allQuery() {
    return new DefaultAllQueryFactory(tableName);
  }
}
