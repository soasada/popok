package com.popokis.popok.service.db;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Database;
import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.service.Service;

import java.sql.SQLException;

public final class SearchDBService<T> implements Service<Long, T> {

  private final Database db;
  private final BasicRepository<T> repository;
  private final Deserializator<T, FixedCachedRowSet> deserializator;

  public SearchDBService(BasicRepository<T> repository, Deserializator<T, FixedCachedRowSet> deserializator) {
    db = Database.getInstance();
    this.repository = repository;
    this.deserializator = deserializator;
  }

  @Override
  public T call(Long id) {
    FixedCachedRowSet fixedCachedRowSet;

    try {
      fixedCachedRowSet = db.executeQuery(repository.find(id));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    if (!fixedCachedRowSet.isBeforeFirst()) {
      throw new RuntimeException("Not found");
    }

    fixedCachedRowSet.next();

    return deserializator.deserialize(fixedCachedRowSet);
  }
}