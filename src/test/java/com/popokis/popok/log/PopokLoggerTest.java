package com.popokis.popok.log;

import com.popokis.popok.util.log.Utils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PopokLoggerTest extends LoggerTemplateTests {
  @Test
  void DebugLogTest() {
    String msg = "This is a debug test";
    logger.debug(msg);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("msg").equals(msg);
    assert jsonLog.get("level").equals("DEBUG");
  }

  @Test
  void InfoLogTest() {
    String msg = "This is a info test";
    logger.info(msg);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("msg").equals(msg);
    assert jsonLog.get("level").equals("INFO");
  }

  @Test
  void WarnLogTest() {
    String msg = "This is a warn test";
    logger.warn(msg);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("msg").equals(msg);
    assert jsonLog.get("level").equals("WARN");
  }

  @Test
  void ErrorLogTest() {
    String msg = "This is an error test";
    logger.error(msg);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("msg").equals(msg);
    assert jsonLog.get("level").equals("ERROR");
  }

  @Test
  void ExceptionTest() {
    String msg = "This is a test with an exception";

    ExceptionInInitializerError e = new ExceptionInInitializerError("weird");
    logger.error(msg, e);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("msg").equals(msg);
  }

  @Test
  void GetLoggerWithNameTest() {
    String name = "Testing name logger";
    Logger newLogger = PopokLogger.getLogger(name);
    newLogger.info("Testing ctor");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));

    assert jsonLog.get("logger").equals(name.replace(" ", "_"));
  }

  @Test
  void GetLoggerWithClassTest() {
    Logger newLogger = PopokLogger.getLogger(PopokLoggerTest.class);
    newLogger.info("Testing ctor");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));

    assert jsonLog.get("logger").equals(PopokLoggerTest.class.getCanonicalName());
  }

  @Test
  void GetLoggerTest() {
    Logger newLogger = PopokLogger.getLogger();
    newLogger.info("Testing ctor");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));

    assert jsonLog.get("logger").equals(PopokLogger.getLoggerName());
  }

  @Test
  void GetLoggerNameTest() {
    assert PopokLogger.getLoggerName().equals(loggerName.replace(" ", "_"));
  }

  @Test
  void SetLoggerNameTest() {
    if(PopokLogger.getLoggerName() != null) {
      assertThrows(UnsupportedOperationException.class, () -> PopokLogger.setLoggerName("testingLog"));
    }
  }

  @Test
  void IllegalLoggerNameTest() {
    assertThrows(IllegalArgumentException.class, () -> PopokLogger.getLogger("!!!223. "));
  }

  @Test
  void SetLoggerNameAndGetLoggerTest() {
    assertThrows(UnsupportedOperationException.class,
        () -> PopokLogger.setLoggerNameAndGetLogger("testingLog"));
  }

  @Test
  void SetLoggerNameWithClassAndGetLoggerTest() {
    assertThrows(UnsupportedOperationException.class,
        () -> PopokLogger.setLoggerNameAndGetLogger(this.getClass()));
  }

  @Test
  void SetLoggerNameWithClassTest() {
    assertThrows(UnsupportedOperationException.class,
        () -> PopokLogger.setLoggerName(this.getClass()));
  }
}