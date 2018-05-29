package com.popokis.popok.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DefaultFindQueryFactory implements FindQueryFactory {

  private final String tableName;
  private final QueryGenerator queryGenerator;

  public DefaultFindQueryFactory(String tableName, QueryGenerator queryGenerator) {
    this.tableName = tableName;
    this.queryGenerator = queryGenerator;
  }

  @Override
  public Query find(long id) {
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM " + tableName + " WHERE " + queryGenerator.putQuestionMark("id");
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
