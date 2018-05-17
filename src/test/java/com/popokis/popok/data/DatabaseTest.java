package com.popokis.popok.data;

import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.db.ListDeserializator;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.deserializator.TestModelDeserializator;
import com.popokis.popok.util.data.model.TestModel;
import com.popokis.popok.util.query.TestRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    long id = insert();
    FixedCachedRowSet fixedCachedRowSet = Database.getInstance().executeQuery(testRepository.find(id));
    fixedCachedRowSet.next();
    String name = fixedCachedRowSet.getString("name");

    assertTrue(id >= 0);
    assertEquals("test", name);
  }

  @Test
  void insertAndDeleteTest() throws SQLException {
    long id = insert();
    int affectedRow = Database.getInstance().executeDML(testRepository.remove(id));

    assertEquals(1, affectedRow);
  }

  @Test
  void insertAndUpdateTest() throws SQLException {
    String expectedName = "test2";

    long id = insert();
    int affectedRow = Database.getInstance().executeDML(testRepository.modify(TestModel.create(id, expectedName)));
    FixedCachedRowSet fixedCachedRowSet = Database.getInstance().executeQuery(testRepository.find(id));
    fixedCachedRowSet.next();

    assertEquals(1, affectedRow);
    assertEquals(expectedName, fixedCachedRowSet.getString("name"));
  }

  @Test
  void insertAndGetAllTest() throws SQLException {
    insert();
    FixedCachedRowSet fixedCachedRowSet = Database.getInstance().executeQuery(testRepository.all());
    Deserializator<List<TestModel>, FixedCachedRowSet> deserializator = new ListDeserializator<>(new TestModelDeserializator());
    List<TestModel> testModels = deserializator.deserialize(fixedCachedRowSet);

    assertFalse(testModels.isEmpty());
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    testRepository = null;
    DatabaseUtil.dropTestSchema();
  }

  private long insert() throws SQLException {
    return Database.getInstance().executeInsert(testRepository.save(TestModel.create(null, "test")));
  }
}
