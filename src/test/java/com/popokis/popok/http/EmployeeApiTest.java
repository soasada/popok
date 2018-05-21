package com.popokis.popok.http;

import com.popokis.popok.http.client.Client;
import com.popokis.popok.http.client.SimpleClient;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.http.FakeServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class EmployeeApiTest {

  private static Client<String> CLIENT;
  private static FakeServer FAKE_SERVER;

  @BeforeAll
  static void initAll() {
    DatabaseUtil.createTestSchema();
    CLIENT = SimpleClient.getInstance();
    FAKE_SERVER = new FakeServer();
  }



  @AfterAll
  static void tearDownAll() {
    DatabaseUtil.dropTestSchema();
    CLIENT = null;
    FAKE_SERVER.stop();
  }
}
