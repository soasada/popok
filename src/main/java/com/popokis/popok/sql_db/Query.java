package com.popokis.popok.sql_db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Query {
  String query();
  void parameters(PreparedStatement stm) throws SQLException;
}
