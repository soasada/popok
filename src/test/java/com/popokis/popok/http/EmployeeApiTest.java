package com.popokis.popok.http;

import com.popokis.popok.http.client.Client;
import com.popokis.popok.http.client.SimpleClient;
import com.popokis.popok.http.server.SimpleServer;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.http.TestingRouter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class EmployeeApiTest {

  private static Client<String> CLIENT;
  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    DatabaseUtil.createTestSchema();
    CLIENT = SimpleClient.getInstance();
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }



  @AfterAll
  static void tearDownAll() {
    DatabaseUtil.dropTestSchema();
    CLIENT = null;
    TESTING_SERVER.stop();
  }
}
