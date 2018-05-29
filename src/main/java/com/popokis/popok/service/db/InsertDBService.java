package com.popokis.popok.service.db;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.query.InsertQueryFactory;
import com.popokis.popok.service.Service;

public final class InsertDBService<T> implements Service<T, Long> {

  private final Database db;
  private final InsertQueryFactory<T> insertQueryFactory;

  public InsertDBService(Database db, InsertQueryFactory<T> insertQueryFactory) {
    this.db = db;
    this.insertQueryFactory = insertQueryFactory;
  }

  @Override
  public Long call(T model) {
    return db.executeInsert(insertQueryFactory.insert(model));
  }
}