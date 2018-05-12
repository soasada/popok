package com.popokis.popok.data;

import java.sql.Connection;

public interface ConnectionPool<T> {
  T pool();
  Connection getConnection();
}
