package com.popokis.popok.service.db;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.ListMapper;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.data.query.AllQueryFactory;
import com.popokis.popok.service.Service;

import java.util.List;

public final class DefaultAllService<T> implements Service<Void, List<T>> {

  private final Database db;
  private final AllQueryFactory queryFactory;
  private final Mapper<List<T>> listMapper;

  public DefaultAllService(Database db, AllQueryFactory queryFactory, Mapper<T> modelMapper) {
    this.db = db;
    this.queryFactory = queryFactory;
    listMapper = new ListMapper<>(modelMapper);
  }

  @Override
  public List<T> call(Void payload) {
    FixedCachedRowSet fixedCachedRowSet;

    fixedCachedRowSet = db.executeQuery(queryFactory.all());
    return listMapper.map(fixedCachedRowSet);
  }
}
