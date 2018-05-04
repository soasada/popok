package com.popokis.popok.util;

import com.popokis.popok.data.Database;
import com.popokis.popok.util.query.CreateTableQuery;
import com.popokis.popok.util.query.DropTableQuery;

import java.sql.SQLException;

public final class DatabaseUtil {

  private DatabaseUtil() {}

  public static void createTestTable() throws SQLException {
    Database.getInstance().executeDML(new CreateTableQuery());
  }

  public static void dropTestTable() throws SQLException {
    Database.getInstance().executeDML(new DropTableQuery());
  }
}
