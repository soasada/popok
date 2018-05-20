package com.popokis.popok.service.db;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Database;
import com.popokis.popok.service.Service;

public final class UpdateDBService<T> implements Service<T, Integer> {

  private final Database db;
  private final BasicRepository<T> repository;

  public UpdateDBService(BasicRepository<T> repository) {
    db = Database.getInstance();
    this.repository = repository;
  }

  @Override
  public Integer call(T model) {
    return db.executeDML(repository.modify(model));
  }
}