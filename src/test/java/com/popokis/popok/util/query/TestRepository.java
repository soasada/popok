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
import com.popokis.popok.util.data.model.TestModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class TestRepository implements BasicRepository<TestModel> {

  private final String tableName;
  private final QueryGenerator queryGenerator;

  public TestRepository() {
    tableName = "test";
    queryGenerator = new QueryGenerator("");
  }

  @Override
  public InsertQueryFactory<TestModel> saveQuery() {
    return model -> new Query() {
      @Override
      public String query() {
        return "INSERT INTO " + tableName + " (name) VALUES(?)";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, model.name());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public UpdateQueryFactory<TestModel> modifyQuery() {
    return model -> new Query() {
      @Override
      public String query() {
        return "UPDATE " + tableName + " SET name = ? WHERE id = ?";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, model.name());
          stm.setLong(2, model.id());
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
