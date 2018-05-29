package com.popokis.popok.data.query;

import java.sql.PreparedStatement;

public final class DefaultAllQueryFactory implements AllQueryFactory {

  private final String tableName;

  public DefaultAllQueryFactory(String tableName) {
    this.tableName = tableName;
  }

  @Override
  public Query all() {
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM " + tableName;
      }

      @Override
      public void parameters(PreparedStatement stm) {

      }
    };
  }
}
