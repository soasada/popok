package com.popokis.popok.log.context;

import com.popokis.popok.log.LoggerTemplateTests;
import org.junit.jupiter.api.Test;

public class LoggerJsonTest extends LoggerTemplateTests {

  @Test
  void JsonLogTest() {
    String jsonString = "{\"string attribute\": \"string value\", \"int attribute\": 1}";

    LoggerContext.addToContext("example", jsonString);

    logger.info("This log contains another JSON");
  }
}
