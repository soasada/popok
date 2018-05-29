package com.popokis.popok.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DefaultDeleteQueryFactory implements DeleteQueryFactory {

  private final String tableName;
  private final QueryGenerator queryGenerator;

  public DefaultDeleteQueryFactory(String tableName, QueryGenerator queryGenerator) {
    this.tableName = tableName;
    this.queryGenerator = queryGenerator;
  }

  @Override
  public Query delete(long id) {
    return new Query() {
      @Override
      public String query() {
        return "DELETE FROM " + tableName + " WHERE " + queryGenerator.putQuestionMark("id") + "LIMIT 1";
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
}
