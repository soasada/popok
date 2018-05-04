package com.popokis.popok.util.query;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SelectQuery implements Query {

  private final long id;

  public SelectQuery(long id) {
    this.id = id;
  }

  @Override
  public String query() {
    return "SELECT * FROM test WHERE id = ?";
  }

  @Override
  public void parameters(PreparedStatement stm) {
    try {
      stm.setLong(1, id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
