package com.popokis.popok.util.query.schema;

import com.popokis.popok.data.query.Query;

import java.sql.PreparedStatement;

public final class DropTestTableQuery implements Query {
  @Override
  public String query() {
    return "DROP TABLE IF EXISTS test";
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
