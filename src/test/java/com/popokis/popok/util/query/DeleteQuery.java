package com.popokis.popok.util.query;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DeleteQuery implements Query {

  private final long id;

  public DeleteQuery(long id) {
    this.id = id;
  }

  @Override
  public String query() {
    return "DELETE FROM test WHERE id = ? LIMIT 1";
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
