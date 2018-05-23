package com.popokis.popok.http;

import com.popokis.popok.http.client.Client;
import com.popokis.popok.http.client.SimpleClient;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.http.server.SimpleServer;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.http.RestResponseDeserializator;
import com.popokis.popok.serialization.json.JacksonSerializator;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.http.TestingRouter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CompanyApiTest {

  private static Client<String> CLIENT;
  private static SimpleServer TESTING_SERVER;

  @BeforeAll
  static void initAll() {
    DatabaseUtil.createTestSchema();
    CLIENT = SimpleClient.getInstance();
    TESTING_SERVER = new SimpleServer(8080, "0.0.0.0", new TestingRouter());
    TESTING_SERVER.start();
  }

  @Test
  void createCompany() {
    Company apiTestCompany = Company.create(null, "apiTestCompany");
    String serializedCompany = new JacksonSerializator<>().serialize(apiTestCompany);
    String rawResponse = CLIENT.post(TESTING_SERVER.url() + "/company/create", serializedCompany);
    Deserializator<RestResponse<Long>, String> deserializator = new RestResponseDeserializator<>(Long.class);
    RestResponse<Long> restResponse = deserializator.deserialize(rawResponse);

    assertTrue(restResponse.payload() > 0);
  }

  @AfterAll
  static void tearDownAll() {
    DatabaseUtil.dropTestSchema();
    CLIENT = null;
    TESTING_SERVER.stop();
  }
}
