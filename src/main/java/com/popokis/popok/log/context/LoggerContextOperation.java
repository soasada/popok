package com.popokis.popok.log.context;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * This class contains helpful functions to filter SLF4J contexts (MDC, NDC) and
 * avoid any implementation dependency.
 */
public final class LoggerContextOperation {

  private LoggerContextOperation() {}

  /**
   * Given a Map this function will return a List with all the values that
   * belongs to the NDC context.
   * @param map map to be filtered
   * @return list of filtered values
   */
  public static Collection<String> getNDCList(Map<String, String> map) {
    return filterNDC(map).entrySet().stream()
        .map(Map.Entry::getValue)
        .collect(toList());
  }

  /**
   * Given a Map this function will return all the entries that belongs to
   * the NDC context.
   * @param map to be filtered
   * @return filtered map
   */
  public static Map<String, String> filterNDC(Map<String, String> map) {
    return map.entrySet().stream()
        .filter(kvp -> kvp.getKey().matches(LoggerNDC.getNDCKeyRegex()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Given a Map this function will return all the Entries that belongs to
   * the MCD context
   * @param map to be filtered
   * @return filtered map
   */
  public static Map<String, String> filterMDC(Map<String, String> map) {
    return map.entrySet().stream()
        .filter(kvp -> !kvp.getKey().matches(LoggerNDC.getNDCKeyRegex()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
