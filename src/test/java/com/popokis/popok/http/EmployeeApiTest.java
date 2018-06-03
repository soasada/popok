package com.popokis.popok.http;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.HikariConnectionPool;
import com.popokis.popok.http.client.Client;
import com.popokis.popok.http.client.SimpleClient;
import com.popokis.popok.http.server.SimpleServer;
import com.popokis.popok.util.data.BootstrapDatabase;
import com.popokis.popok.util.http.TestingRouter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class EmployeeApiTest {

  private static Database db;
  private static Client<String> CLIENT;
  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    db = new Database(HikariConnectionPool.getInstance());
    BootstrapDatabase.createTestSchema(db);
    CLIENT = SimpleClient.getInstance();
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }



  @AfterAll
  static void tearDownAll() {
    BootstrapDatabase.dropTestSchema(db);
    CLIENT = null;
    TESTING_SERVER.stop();
    db = null;
  }
}
