package com.popokis.popok.data.query;

public interface UpdateQueryFactory<T> {
  Query update(T model);
}
