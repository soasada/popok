package com.popokis.popok.dummy;

import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;

public final class InsertQuery implements Query {
  @Override
  public String query() {
    return "INSERT INTO test (name) VALUES('test')";
  }

  @Override
  public void parameters(PreparedStatement stm) {

  }
}
