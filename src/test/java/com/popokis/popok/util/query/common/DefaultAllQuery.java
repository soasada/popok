package com.popokis.popok.util.query.common;

import com.popokis.popok.data.Query;

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
