package com.popokis.popok.service.db;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.query.DeleteQueryFactory;
import com.popokis.popok.service.Service;

public final class RemoveDBService<T> implements Service<Long, Integer> {

  private final Database db;
  private final DeleteQueryFactory deleteQueryFactory;

  public RemoveDBService(Database db, DeleteQueryFactory deleteQueryFactory) {
    this.db = db;
    this.deleteQueryFactory = deleteQueryFactory;
  }

  @Override
  public Integer call(Long id) {
    return db.executeDML(deleteQueryFactory.delete(id));
  }
}
