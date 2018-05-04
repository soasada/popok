package com.popokis.popok.dummy;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SelectQuery implements Query {
  @Override
  public String query() {
    return "SELECT * FROM test WHERE id = ?";
  }

  @Override
  public void parameters(PreparedStatement stm) {
    try {
      stm.setLong(1, 1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
