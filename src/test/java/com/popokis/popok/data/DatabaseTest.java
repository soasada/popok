package com.popokis.popok.data;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.access.HikariConnectionPool;
import com.popokis.popok.data.mapper.ListMapper;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.util.data.BootstrapDatabase;
import com.popokis.popok.util.data.mapper.TestModelMapper;
import com.popokis.popok.util.data.model.TestModel;
import com.popokis.popok.util.query.TestRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.popokis.popok.util.data.FakeData.TEST_DELETE_ID;
import static com.popokis.popok.util.data.FakeData.TEST_FIND_ID;
import static com.popokis.popok.util.data.FakeData.TEST_MODIFY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest {

  private static BasicRepository<TestModel> testRepository;
  private static Database db;

  @BeforeAll
  static void initAll() {
    testRepository = new TestRepository();
    db = new Database(HikariConnectionPool.getInstance());
    BootstrapDatabase.createTestSchema(db);
  }

  @Test
  void insertTest() {
    long id = db.executeInsert(testRepository.saveQuery().insert(TestModel.create(null, "test")));
    assertTrue(id >= 0);
  }

  @Test
  void findTest() {
    FixedCachedRowSet fixedCachedRowSet = db.executeQuery(testRepository.findQuery().find(TEST_FIND_ID));
    fixedCachedRowSet.next();
    String name = fixedCachedRowSet.getString("name");

    assertEquals("findTest", name);
  }

  @Test
  void deleteTest() {
    int affectedRow = db.executeDML(testRepository.removeQuery().delete(TEST_DELETE_ID));

    assertEquals(1, affectedRow);
  }

  @Test
  void updateTest() {
    String expectedName = "test2";
    int affectedRow = db.executeDML(testRepository.modifyQuery().update(TestModel.create(TEST_MODIFY_ID, expectedName)));

    assertEquals(1, affectedRow);
  }

  @Test
  void getAllTest() {
    FixedCachedRowSet fixedCachedRowSet = db.executeQuery(testRepository.allQuery().all());
    Mapper<List<TestModel>> mapper = new ListMapper<>(new TestModelMapper());
    List<TestModel> testModels = mapper.map(fixedCachedRowSet);

    assertFalse(testModels.isEmpty());
  }

  @AfterAll
  static void tearDownAll() {
    testRepository = null;
    BootstrapDatabase.dropTestSchema(db);
    db = null;
  }
}
