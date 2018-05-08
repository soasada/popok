package com.popokis.popok.http.client;

import com.popokis.popok.util.http.FakeServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonClientTest {

  private static Client<String> CLIENT;
  private static FakeServer FAKE_SERVER;

  @BeforeAll
  static void initAll() {
    CLIENT = JsonClient.getInstance();
    FAKE_SERVER = new FakeServer();
  }

  @Test
  void syncGetTest() {
    String response = CLIENT.get(FAKE_SERVER.url() + "/fake/get?key=popokis");
    assertTrue(response.contains("popokis"));
  }

  @Test
  void syncPostTest() {
    String response = CLIENT.post(FAKE_SERVER.url() + "/fake/post", "popokis");
    assertTrue(response.contains("popokis"));
  }

  @AfterAll
  static void tearDownAll() {
    CLIENT = null;
    FAKE_SERVER.stop();
  }
}