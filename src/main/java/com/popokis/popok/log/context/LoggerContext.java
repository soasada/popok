package com.popokis.popok.log.context;

import org.slf4j.MDC;
import org.slf4j.NDC;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * This class contains helpful functions to manage SLF4J contexts (MDC, NDC) and
 * avoid any implementation dependency.
 */
public final class LoggerContext {

  private LoggerContext() {}

  /**
   * This method will clear MDC and NDC contexts.
   */
  public static void clearContext() {
    clearContextMap();
    clearContextStack();
  }

  /**
   * This method will clear MDC context only
   */
  public static void clearContextMap() {
    Collection<String> contextStack = getContextStack();
    MDC.clear();
    addToContext(contextStack);
  }

  /**
   * This method will clear NDC context only
   */
  public static void clearContextStack() {
    String value = NDC.pop();
    while(!value.isEmpty()) value = NDC.pop();
  }

  /**
   * Remove a value with @key from MDC context
   * @param key key to be removed
   */
  public static void removeFromContext(String key) {
    MDC.remove(key);
  }

  /**
   * Remove a list of values from MDC context
   * @param keys list of keys to be removed
   */
  public static void removeFromContext(Iterable<String> keys) {
    keys.forEach(LoggerContext::removeFromContext);
  }

  /**
   * This method is not typesafe, we will cast to string
   * every item in collection c and then add them to the NDC context.
   *
   * @param c collection to be added
   */
  @SuppressWarnings("unchecked")
  public static void addCollectionToContext(Collection c) {
    c.forEach(e -> NDC.push(e.toString()));
  }

  /**
   * Add a collection of values to NDC context
   * @param c String collection to be added
   */

  public static void addToContext(Collection<String> c) {
    c.forEach(NDC::push);
  }

  /**
   * Add mapped values to MDC context
   * @param m map to be added
   */
  public static void addToContext(Map<String, String> m) {
    Map<String, String> ctxMap = MDC.getCopyOfContextMap();
    ctxMap.putAll(m);
    MDC.setContextMap(ctxMap);
  }

  /**
   * This method is not typesafe, we will try to cast
   * to string every key and value of within the map
   * @param m map to be added
   */
  @SuppressWarnings("unchecked")
  public static void addMapToContext(Map m) {
    Map<String, String> ctxMap = MDC.getCopyOfContextMap();
    ctxMap.putAll(m);
    MDC.setContextMap(ctxMap);
  }

  /**
   * Add value to NDC context
   * @param value to be added
   */
  public static void addToContext(String value) {
    NDC.push(value);
  }

  /**
   * Add value to MDC context
   * @param key to be added
   * @param value to be added
   */
  public static void addToContext(String key, String value) {
    MDC.put(key, value);
  }

  private static String getFromContext(String key) {
    return MDC.get(key);
  }

  /**
   * Add map Entry to MDC context
   * @param e map entry to be added
   */
  public static void addToContext(Map.Entry e) {
    MDC.put(e.getKey().toString(), e.getValue().toString());
  }

  /**
   * Add key values pairs to MDC
   * @param args list of key value to be added
   */
  public static void addToContextMap(String... args) {
    if (args == null || args.length % 2 != 0)
      throw new IllegalArgumentException("You need an even amount of arguments to Map them.");

    IntStream.range(0, args.length)
        .filter(n -> n % 2 == 0)
        .forEach(i -> addToContext(args[i], args[i+1]));
  }

  /**
   * Add values to NDC
   * @param args list of values o be added
   */
  public static void addToContextStack(String... args) {
    Arrays.stream(args).forEach(NDC::push);
  }

  /**
   * Get a copy of MDC map
   *
   * @return filtered context map
   */
  public static Map<String, String> getContextMap() {
    return LoggerContextOperation.filterMDC(MDC.getCopyOfContextMap());
  }

  /**
   * Get a copy of the NDC value list
   * @return filtered context stack
   */
  public static Collection<String> getContextStack() {
    return LoggerContextOperation.getNDCList(MDC.getCopyOfContextMap());
  }
}