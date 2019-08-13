package com.popokis.popok.sql_db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class QueryFactory {

  private QueryFactory() {}

  public static Query create(String query) {
    return new Query() {
      @Override
      public String query() {
        return query;
      }

      @Override
      public void parameters(PreparedStatement stm) throws SQLException {

      }
    };
  }
}
