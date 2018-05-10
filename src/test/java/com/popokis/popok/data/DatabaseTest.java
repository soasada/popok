package com.popokis.popok.data;

import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.model.TestModel;
import com.popokis.popok.util.query.TestRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

  private static BasicRepository<TestModel> testRepository;

  @BeforeAll
  static void initAll() throws SQLException {
    testRepository = new TestRepository();
    DatabaseUtil.createTestSchema();
  }

  @Test
  void insertAndSelectTest() throws SQLException {
    long id = Database.getInstance().executeInsert(testRepository.save(TestModel.create(null, "test")));
    CachedRowSet cachedRowSet = Database.getInstance().executeQuery(testRepository.find(id));
    cachedRowSet.next();
    String name = cachedRowSet.getString("name");

    assertTrue(id >= 0);
    assertEquals("test", name);
  }

  @Test
  void insertAndDeleteTest() throws SQLException {
    long id = Database.getInstance().executeInsert(testRepository.save(TestModel.create(null, "test")));
    int affectedRow = Database.getInstance().executeDML(testRepository.remove(id));

    assertEquals(1, affectedRow);
  }

  @Test
  void insertAndUpdateTest() throws SQLException {
    String expectedName = "test2";

    long id = Database.getInstance().executeInsert(testRepository.save(TestModel.create(null, "test")));
    int affectedRow = Database.getInstance().executeDML(testRepository.modify(TestModel.create(id, expectedName)));
    CachedRowSet cachedRowSet = Database.getInstance().executeQuery(testRepository.find(id));
    cachedRowSet.next();

    assertEquals(1, affectedRow);
    assertEquals(expectedName, cachedRowSet.getString("name"));
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    DatabaseUtil.dropTestSchema();
  }
}