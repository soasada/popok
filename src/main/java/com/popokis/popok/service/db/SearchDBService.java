package com.popokis.popok.service.db;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.data.query.FindQueryFactory;
import com.popokis.popok.service.Service;

public final class SearchDBService<T> implements Service<Long, T> {

  private final Database db;
  private final FindQueryFactory findQueryFactory;
  private final Mapper<T> mapper;

  public SearchDBService(Database db, FindQueryFactory findQueryFactory, Mapper<T> mapper) {
    this.db = db;
    this.findQueryFactory = findQueryFactory;
    this.mapper = mapper;
  }

  @Override
  public T call(Long id) {
    FixedCachedRowSet fixedCachedRowSet;

    fixedCachedRowSet = db.executeQuery(findQueryFactory.find(id));

    if (!fixedCachedRowSet.isBeforeFirst()) {
      throw new RuntimeException("Not found");
    }

    return mapper.map(fixedCachedRowSet);
  }
}