package com.popokis.popok.util.query;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class UpdateQuery implements Query {

  private final String newName;
  private final long id;

  public UpdateQuery(String newName, long id) {
    this.newName = newName;
    this.id = id;
  }

  @Override
  public String query() {
    return "UPDATE test SET name = ? WHERE id = ?";
  }

  @Override
  public void parameters(PreparedStatement stm) {
    try {
      stm.setString(1, newName);
      stm.setLong(2, id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
