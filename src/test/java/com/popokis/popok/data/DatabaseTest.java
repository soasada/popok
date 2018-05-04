package com.popokis.popok.data;

import com.popokis.popok.dummy.CreateTableQuery;
import com.popokis.popok.dummy.DropTableQuery;
import com.popokis.popok.dummy.InsertQuery;
import com.popokis.popok.dummy.SelectQuery;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {
  @Test
  void insertAndSelectTest() {
    long id = -1L;
    String name = "";

    try {
      Database.getInstance().executeDML(new DropTableQuery());
      Database.getInstance().executeDML(new CreateTableQuery());
      id = Database.getInstance().executeInsert(new InsertQuery());
      CachedRowSet cachedRowSet = Database.getInstance().executeQuery(new SelectQuery());
      cachedRowSet.next();
      name = cachedRowSet.getString("name");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    assertEquals(1L, id);
    assertEquals("test", name);
  }
}