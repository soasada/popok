package com.popokis.popok.log;

import com.popokis.popok.log.common.LoggerUtils;
import com.popokis.popok.log.context.LoggerContext;
import com.popokis.popok.util.log.ListAppender;
import com.popokis.popok.util.log.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoggerTemplateTests {

  protected ListAppender appender;
  protected Logger logger;
  static String loggerName = "Logger Tests";

  @BeforeAll
  static void instantiateLogger() {
    if(PopokLogger.getLoggerName() == null) {
      assertThrows(IllegalStateException.class, PopokLogger::getLogger);
      PopokLogger.setLoggerName(loggerName);
      assert PopokLogger.getLoggerName().equals(loggerName.replace(" ", "_"));
    }
  }

  @BeforeEach
  void setUp() {
    this.appender = ListAppender.getListAppender("ListAppender");
    this.logger = PopokLogger.getLogger();
  }

  @AfterEach
  void cleanUp() {
    LoggerContext.clearContext();
    // before cleaning the appender we check every generated message to be valid json
    assert appender.getMessages()
        .stream()
        .allMatch(l -> LoggerUtils.isValidJSON(l) && Utils.logHasAllInfo(l));

    appender.clear();
  }
}
