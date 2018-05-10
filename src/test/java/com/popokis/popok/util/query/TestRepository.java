package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;
import com.popokis.popok.util.data.model.TestModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class TestRepository implements BasicRepository<TestModel> {

  @Override
  public Query save(TestModel model) {
    return new Query() {
      @Override
      public String query() {
        return "INSERT INTO test (name) VALUES(?)";
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
        return "UPDATE test SET name = ? WHERE id = ?";
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
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM test WHERE id = ?";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setLong(1, id);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query remove(long id) {
    return new Query() {
      @Override
      public String query() {
        return "DELETE FROM test WHERE id = ? LIMIT 1";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setLong(1, id);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query all() {
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM test";
      }

      @Override
      public void parameters(PreparedStatement stm) {

      }
    };
  }
}
