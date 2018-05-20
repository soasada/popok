package com.popokis.popok.data.query;

import java.sql.PreparedStatement;

public final class DefaultAllQuery implements Query {

  private final String tableName;

  public DefaultAllQuery(String tableName) {
    this.tableName = tableName;
  }

  @Override
  public String query() {
    return "SELECT * FROM " + tableName;
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
