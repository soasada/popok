package com.popokis.popok.dummy;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;

public final class DropTableQuery implements Query {
  @Override
  public String query() {
    return "DROP TABLE IF EXISTS test";
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
