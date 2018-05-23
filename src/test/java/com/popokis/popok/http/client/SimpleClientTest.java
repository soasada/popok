package com.popokis.popok.http.client;

import com.popokis.popok.http.server.SimpleServer;
import com.popokis.popok.util.http.TestingRouter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleClientTest {

  private static Client<String> CLIENT;
  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    CLIENT = SimpleClient.getInstance();
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }

  @Test
  void syncGetTest() {
    String response = CLIENT.get(TESTING_SERVER.url() + "/fake/get?key=popokis");
    assertTrue(response.contains("popokis"));
  }

  @Test
  void syncPostTest() {
    String response = CLIENT.post(TESTING_SERVER.url() + "/fake/post", "popokis");
    assertTrue(response.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    CLIENT = null;
    TESTING_SERVER.stop();
  }
}