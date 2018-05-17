package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;
import com.popokis.popok.data.QueryGenerator;
import com.popokis.popok.util.data.model.TestModel;
import com.popokis.popok.util.query.common.DefaultAllQuery;
import com.popokis.popok.util.query.common.DefaultDeleteQuery;
import com.popokis.popok.util.query.common.DefaultFindQuery;

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
  public Query save(TestModel model) {
    return new Query() {
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
  public Query modify(TestModel model) {
    return new Query() {
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
