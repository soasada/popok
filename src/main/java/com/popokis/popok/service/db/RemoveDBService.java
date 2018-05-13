package com.popokis.popok.service.db;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Database;
import com.popokis.popok.service.Service;

import java.sql.SQLException;

public final class RemoveDBService<T> implements Service<Long, Integer> {

  private final Database db;
  private final BasicRepository<T> repository;

  public RemoveDBService(BasicRepository<T> repository) {
    db = Database.getInstance();
    this.repository = repository;
  }

  @Override
  public Integer call(Long id) {
    try {
      return db.executeDML(repository.remove(id));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
