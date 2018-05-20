package com.popokis.popok.data.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DefaultFindQuery implements Query {

  private final long id;
  private final String tableName;
  private final QueryGenerator queryGenerator;

  public DefaultFindQuery(long id, String tableName, QueryGenerator queryGenerator) {
    this.id = id;
    this.tableName = tableName;
    this.queryGenerator = queryGenerator;
  }

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
}
