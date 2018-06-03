package com.popokis.popok.util.query;

import com.popokis.popok.data.query.Query;

import java.sql.PreparedStatement;

public final class TestingQueryFactory {

  private TestingQueryFactory() {}

  public static Query create(String query) {
    return new Query() {
      @Override
      public String query() {
        return query;
      }

      @Override
      public void parameters(PreparedStatement preparedStatement) {

      }
    };
  }

}
