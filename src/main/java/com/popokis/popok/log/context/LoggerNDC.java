package com.popokis.popok.log.context;

import org.slf4j.NDC;

public final class LoggerNDC {

  private static final String NDC_PREFIX = NDC.PREFIX;
  private static final String NDC_KEY_REGEX = "(^" + NDC_PREFIX + "\\d*$)";

  private LoggerNDC() {}

  public static String getNDCPrefix() {
    return NDC_PREFIX;
  }

  public static String getNDCKeyRegex() {
    return NDC_KEY_REGEX;
  }
}
