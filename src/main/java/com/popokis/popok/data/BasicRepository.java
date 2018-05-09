package com.popokis.popok.data;

public interface BasicRepository<T> {
  Query save(T model);
  Query modify(T model);
  Query find(long id);
  Query remove(long id);
  Query all();
}
