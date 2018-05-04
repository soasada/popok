package com.popokis.popok.data;

import com.popokis.popok.util.DatabaseUtil;
import com.popokis.popok.util.query.DeleteQuery;
import com.popokis.popok.util.query.InsertQuery;
import com.popokis.popok.util.query.SelectQuery;
import com.popokis.popok.util.query.UpdateQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

  @BeforeAll
  static void initAll() throws SQLException {
    DatabaseUtil.createTestTable();
  }

  @Test
  void insertAndSelectTest() throws SQLException {
    long id = Database.getInstance().executeInsert(new InsertQuery());
    CachedRowSet cachedRowSet = Database.getInstance().executeQuery(new SelectQuery(id));
    cachedRowSet.next();
    String name = cachedRowSet.getString("name");

    assertTrue(id >= 0);
    assertEquals("test", name);
  }

  @Test
  void insertAndDeleteTest() throws SQLException {
    long id = Database.getInstance().executeInsert(new InsertQuery());
    int affectedRow = Database.getInstance().executeDML(new DeleteQuery(id));

    assertEquals(1, affectedRow);
  }

  @Test
  void insertAndUpdateTest() throws SQLException {
    String expectedName = "test2";

    long id = Database.getInstance().executeInsert(new InsertQuery());
    int affectedRow = Database.getInstance().executeDML(new UpdateQuery(expectedName, id));
    CachedRowSet cachedRowSet = Database.getInstance().executeQuery(new SelectQuery(id));
    cachedRowSet.next();

    assertEquals(1, affectedRow);
    assertEquals(expectedName, cachedRowSet.getString("name"));
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    DatabaseUtil.dropTestTable();
  }
}
