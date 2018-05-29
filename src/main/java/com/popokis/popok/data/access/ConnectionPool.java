package com.popokis.popok.data.access;

import java.sql.Connection;

public interface ConnectionPool<T> {
  T pool();
  Connection getConnection();
}
