package com.popokis.popok.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PopokLogger {

  private static String loggerName;

  private PopokLogger() {}

  /**
   * Set the class static logger name that will be used for all future loggers
   * (this is important as it will be the log file name on the centralized logger server
   * @param loggerName String logger name (also will be the logger filename in the logger server)
   */
  public static void setLoggerName(String loggerName) {
    String name = cleanAndValidateLoggerName(loggerName);

    if (PopokLogger.loggerName == null) {
      PopokLogger.loggerName = name;
    } else {
      throw new UnsupportedOperationException("Logger name is already set, you can only do it once.");
    }
  }

  public static String getLoggerName() {
    return loggerName;
  }

  /**
   * Set the class static logger name that will be used for all future loggers and returns the logger
   * instance
   * @param loggerName String logger name
   * @return Logger instance with the logger name
   */
  public static Logger setLoggerNameAndGetLogger(String loggerName) {
    setLoggerName(loggerName);
    return getLogger(PopokLogger.loggerName);
  }

  /**
   * See {@link #setLoggerNameAndGetLogger(String)}
   * @param clazz class name for the logger name
   * @return Logger instance with the class logger name
   */
  public static Logger setLoggerNameAndGetLogger(Class<?> clazz) {
    setLoggerName(clazz);
    return getLogger(loggerName);
  }

  /**
   * Set the class static logger name that will be used for all future loggers
   * (this is important as it will be the log file name on the centralized logger server
   * @param clazz name for the logger name (also will be the logger filename in the logger server)
   */
  public static void setLoggerName(Class<?> clazz) {
    setLoggerName(clazz.getCanonicalName());
  }

  /**
   * THIS IS THE PREFERED WAY OF GETTING THE LOGGER
   * This function will return a Logger instance with the proper project logger name if it has been inicialized,
   * otherwise will throw a IllegalStateException.
   * @return Logger instance with the class logger name
   */
  public static Logger getLogger() {
    if (loggerName == null)
      throw new IllegalStateException("Logger name has not been initialized, use setLoggerName first.");

    return LoggerFactory.getLogger(loggerName);
  }

  /**
   * This function will return a Logger instance using a custom logger name, ONLY USE this function if you strictly
   * need to use a different logger name (and file).
   * @param loggerName for the custom logger
   * @return Logger instance for the custom logger
   */
  public static Logger getLogger(String loggerName) {
    return LoggerFactory.getLogger(cleanAndValidateLoggerName(loggerName));
  }

  /**
   *  See {@link #getLogger(String)}
   * @param clazz class name will be used as name for the custom logger
   * @return Logger instance for the custom logger
   */
  public static Logger getLogger(Class<?> clazz) {
    return getLogger(clazz.getCanonicalName());
  }

  private static String cleanAndValidateLoggerName(String loggerName) {
    String name = cleanLoggerName(loggerName);
    if (!validateLoggerName(name))
      throw new IllegalArgumentException("Logger name is invalid: " + loggerName);

    return name;
  }

  private static String cleanLoggerName(String loggerName) {
    return loggerName.replace(" ", "_");
  }

  private static boolean validateLoggerName(String name) {
    return name.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '.' || c == '_');
  }
}
