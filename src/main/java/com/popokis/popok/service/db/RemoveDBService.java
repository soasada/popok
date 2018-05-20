package com.popokis.popok.service.db;

import com.popokis.popok.data.Database;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.service.Service;

public final class RemoveDBService<T> implements Service<Long, Integer> {

  private final Database db;
  private final BasicRepository<T> repository;

  public RemoveDBService(BasicRepository<T> repository) {
    db = Database.getInstance();
    this.repository = repository;
  }

  @Override
  public Integer call(Long id) {
    return db.executeDML(repository.remove(id));
  }
}
