package com.popokis.popok.service.db;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.query.UpdateQueryFactory;
import com.popokis.popok.service.Service;

public final class UpdateDBService<T> implements Service<T, Integer> {

  private final Database db;
  private final UpdateQueryFactory<T> updateQueryFactory;

  public UpdateDBService(Database db, UpdateQueryFactory<T> updateQueryFactory) {
    this.db = db;
    this.updateQueryFactory = updateQueryFactory;
  }

  @Override
  public Integer call(T model) {
    return db.executeDML(updateQueryFactory.update(model));
  }
}