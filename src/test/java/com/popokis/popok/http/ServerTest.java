package com.popokis.popok.http;

import io.undertow.Handlers;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerTest {

  private static final String HTTP_URL = "http://localhost:8080";
  private static final String HTTPS_URL = "https://localhost:8443";

  @Test
  void shouldReturnHelloWorldResponse() {
    String expected = "Hello World!";
    Server server = Server.builder(
        Handlers.path()
            .addPrefixPath("/api/v1", Handlers.routing()
                .get("/hello", (exchange) -> {
                  exchange.setStatusCode(StatusCodes.OK);
                  exchange.getResponseSender().send("Hello World!");
                })
            )
    )
        .propertiesFilename("app_simple.properties")
        .build();

    server.start();
    String actual = SimpleHttpClient.getInstance().get(HTTP_URL + "/api/v1/hello");
    server.stop();

    assertEquals(expected, actual);
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
}