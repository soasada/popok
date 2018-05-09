package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;

import java.sql.PreparedStatement;

public final class TestRepository implements BasicRepository<String> {

  @Override
  public Query save(String model) {
    return new Query() {
      @Override
      public String query() {
        return "INSERT INTO test (name) VALUES('test')";
      }

      @Override
      public void parameters(PreparedStatement stm) {

      }
    };
  }

  @Override
  public Query modify(String model) {
    return null;
  }

  @Override
  public Query find(long id) {
    return null;
  }

  @Override
  public Query remove(long id) {
    return null;
  }

  @Override
  public Query all() {
    return null;
  }
}
