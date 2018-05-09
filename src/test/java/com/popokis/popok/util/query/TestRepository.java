package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;

public final class TestRepository implements BasicRepository<String> {
  @Override
  public Query save(String model) {
    return null;
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
