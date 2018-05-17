package com.popokis.popok.service.db;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Database;
import com.popokis.popok.service.Service;

import java.sql.SQLException;

public final class UpdateDBService<T> implements Service<T, Integer> {

  private final Database db;
  private final BasicRepository<T> repository;

  public UpdateDBService(BasicRepository<T> repository) {
    db = Database.getInstance();
    this.repository = repository;
  }

  @Override
  public Integer call(T model) {
    try {
      return db.executeDML(repository.modify(model));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}