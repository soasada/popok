package com.popokis.popok.data.query;

public interface BasicRepository<T> {
  InsertQueryFactory<T> saveQuery();
  UpdateQueryFactory<T> modifyQuery();
  FindQueryFactory findQuery();
  DeleteQueryFactory removeQuery();
  AllQueryFactory allQuery();
}
