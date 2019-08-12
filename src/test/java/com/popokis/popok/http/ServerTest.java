package com.popokis.popok.http;

import io.undertow.Handlers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTest {

  private static final String HTTP_URL = "http://localhost:8081";
  private static final String HTTPS_URL = "https://localhost:8443";
  private static final Server HTTP_SERVER = Server.builder(TestRouter.of())
      .propertiesFilename("app_simple.properties")
      .build();
  private static final Server HTTPS_SERVER = Server.builder(TestRouter.of())
      .propertiesFilename("app_simple_https.properties")
      .enableHttps()
      .keyStorePath("keystore.jks")
      .enableHttp2()
      .redirectToHttps()
      .build();

  @BeforeEach
  void setUp() {
    HTTP_SERVER.start();
    HTTPS_SERVER.start();
  }

  @AfterEach
  void tearDown() {
    HTTP_SERVER.stop();
    HTTPS_SERVER.stop();
  }

  @Test
  void shouldReturnHelloWorldResponse() {
    String expected = "Hello World!";
    HttpResponse<String> actual = SimpleHttpClient.getInstance().get(HTTP_URL + "/api/v1/hello");

    assertEquals(expected, actual.body());
  }

  @Test
  void shouldThrowAnExceptionWhenEmptyConfigFile() {
    assertThrows(RuntimeException.class, () -> Server.builder(Handlers.path())
        .propertiesFilename("app_empty.properties")
        .build()
    );
  }

  @Test
  void shouldThrowAnExceptionWhenMissingHttpPortProperty() {
    String expected = "server.http.port property not found.";
    try {
      Server.builder(Handlers.path())
          .propertiesFilename("app_missing_http_port.properties")
          .build();
    } catch (RuntimeException e) {
      String actual = e.getMessage();
      assertEquals(expected, actual);
    }
  }

  @Test
  void shouldThrowAnExceptionWhenMissingAddressProperty() {
    String expected = "server.address property not found.";
    try {
      Server.builder(Handlers.path())
          .propertiesFilename("app_missing_address.properties")
          .build();
    } catch (RuntimeException e) {
      String actual = e.getMessage();
      assertEquals(expected, actual);
    }
  }

  @Test
  void shouldLetHttpsRequests() {
    String expected = "Hello World!";
    HttpResponse<String> actual = SimpleHttpClient.getInstance().get(HTTPS_URL + "/api/v1/hello");

    assertEquals(expected, actual.body());
  }

  @Test
  void shouldRedirectToHttps() {
    String expected = "Hello World!";
    HttpResponse<String> actual = SimpleHttpClient.getInstance().get("http://localhost:8080/api/v1/hello");

    assertTrue(actual.headers().map().containsKey("x-undertow-transport"));
    assertEquals("h2", actual.headers().map().get("x-undertow-transport").get(0));
    assertEquals(expected, actual.body());
  }
}