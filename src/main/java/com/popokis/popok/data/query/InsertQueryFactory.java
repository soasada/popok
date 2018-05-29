package com.popokis.popok.data.query;

public interface InsertQueryFactory<T> {
  Query insert(T model);
}
