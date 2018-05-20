package com.popokis.popok.service.db;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Database;
import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.db.ListDeserializator;
import com.popokis.popok.service.Service;

import java.util.List;

public final class DefaultAllService<T> implements Service<Void, List<T>> {

  private final Database db;
  private final BasicRepository<T> repository;
  private final Deserializator<List<T>, FixedCachedRowSet> listDeserializator;

  public DefaultAllService(BasicRepository<T> repository, Deserializator<T, FixedCachedRowSet> modelDeserializator) {
    db = Database.getInstance();
    this.repository = repository;
    listDeserializator = new ListDeserializator<>(modelDeserializator);
  }

  @Override
  public List<T> call(Void payload) {
    FixedCachedRowSet fixedCachedRowSet;

    fixedCachedRowSet = db.executeQuery(repository.all());
    return listDeserializator.deserialize(fixedCachedRowSet);
  }
}
