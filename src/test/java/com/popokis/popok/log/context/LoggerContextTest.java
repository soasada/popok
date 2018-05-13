package com.popokis.popok.log.context;

import com.popokis.popok.log.LoggerTemplateTests;
import com.popokis.popok.util.log.Utils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LoggerContextTest extends LoggerTemplateTests {

  @Test
  void ClearContextStackTest() {
    Set<Integer> intCollection = Utils.createIntegerSampleSet();

    LoggerContext.addCollectionToContext(intCollection);
    Collection<String> contextStack = LoggerContext.getContextStack();

    assert contextStack.size() == intCollection.size();
    LoggerContext.clearContextStack();
    contextStack = LoggerContext.getContextStack();
    assert contextStack.size() == 0;
  }

  @Test
  void ClearContextStackSideEffectsTest() {
    Set<Integer> intCollection = Utils.createIntegerSampleSet();

    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addCollectionToContext(intCollection);
    LoggerContext.addToContext(testMap);

    Collection<String> contextStack = LoggerContext.getContextStack();
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == intCollection.size();
    assert contextMap.size() == testMap.size();

    LoggerContext.clearContextStack();

    contextStack = LoggerContext.getContextStack();
    contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == 0;
    assert contextMap.size() == testMap.size();
  }

  @Test
  void ClearContextMapTest() {
    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addToContext(testMap);
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert testMap.size() == contextMap.size();
    LoggerContext.clearContextMap();
    contextMap = LoggerContext.getContextMap();
    assert contextMap.size() == 0;
  }

  @Test
  void ClearContextMapSideEffectsTest() {
    Set<Integer> intCollection = Utils.createIntegerSampleSet();

    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addCollectionToContext(intCollection);
    LoggerContext.addToContext(testMap);

    Collection<String> contextStack = LoggerContext.getContextStack();
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == intCollection.size();
    assert contextMap.size() == testMap.size();

    LoggerContext.clearContextMap();

    contextStack = LoggerContext.getContextStack();
    contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == intCollection.size();
    assert contextMap.size() == 0;
  }

  @Test
  void ClearContextTest() {
    Set<Integer> intCollection = Utils.createIntegerSampleSet();

    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addCollectionToContext(intCollection);
    LoggerContext.addToContext(testMap);

    Collection<String> contextStack = LoggerContext.getContextStack();
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == intCollection.size();
    assert contextMap.size() == testMap.size();

    LoggerContext.clearContext();

    contextStack = LoggerContext.getContextStack();
    contextMap = LoggerContext.getContextMap();

    assert contextStack.size() == 0;
    assert contextMap.size() == 0;
  }

  @Test
  void RemoveFromContextTest() {
    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addToContext(testMap);

    Map<String, String> contextMap = LoggerContext.getContextMap();
    assert contextMap.equals(testMap);

    LoggerContext.removeFromContext("uno");
    contextMap = LoggerContext.getContextMap();

    assert !contextMap.equals(testMap);
    assert contextMap.get("uno") == null;
    assert contextMap.get("dos").equals("2");
    assert contextMap.get("tres").equals("3");
  }

  @Test
  void RemoveKeysFromContextTest() {
    Map<String, String> testMap = Utils.createLargeStringSampleMap();

    List<String> keysToRemove = new ArrayList<String>() {{
      add("tres");
      add("cuatro");
      add("cinco");
    }};

    LoggerContext.addToContext(testMap);

    Map<String, String> contextMap = LoggerContext.getContextMap();
    assert contextMap.equals(testMap);

    LoggerContext.removeFromContext(keysToRemove);
    contextMap = LoggerContext.getContextMap();
    assert !contextMap.equals(testMap);

    Map<String, String> finalContextMap = contextMap;
    keysToRemove.forEach(k -> {
      assert !finalContextMap.containsKey(k);
    });
  }

  @Test
  void AddCollectionToContextTest() {
    // we use a set instead of a List to not take into account the order of the collection, just the content
    Set<Integer> intCollection = Utils.createIntegerSampleSet();

    LoggerContext.addCollectionToContext(intCollection);
    logger.info("unit testing log");

    Set<Integer> contextStack = LoggerContext.getContextStack().stream()
        .mapToInt(Integer::valueOf)
        .boxed()
        .collect(Collectors.toSet());

    assert contextStack.equals(intCollection);
  }

  @Test
  void AddStringCollectionToContextTest() {
    List<String> testCollection = Utils.createStringSampleList();

    LoggerContext.addToContext(testCollection);
    Collection<String> contextStack = LoggerContext.getContextStack();

    assert testCollection.containsAll(contextStack);
  }

  @Test
  void AddStringMapToContext() {
    Map<String, String> testMap = Utils.createStringSampleMap();

    LoggerContext.addToContext(testMap);
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextMap.keySet().containsAll(testMap.keySet());
  }

  @Test
  void AddMapToContext() {
    Map<String, Integer> invalidTestMap = Utils.createIntegerSampleMap();

    assertThrows(ClassCastException.class,
        () -> LoggerContext.addMapToContext(invalidTestMap));

    Map<String, String> validTestMap = Utils.createStringSampleMap();

    LoggerContext.addMapToContext(validTestMap);
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextMap.keySet().containsAll(validTestMap.keySet());

    logger.info("unit testing log");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("uno").equals(1);
    assert jsonLog.get("dos").equals(2);
    assert jsonLog.get("tres").equals(3);
  }

  @SuppressWarnings("unchecked")
  @Test
  void AddValueToContextTest() {
    String testValue = "testing string value";
    LoggerContext.addToContext(testValue);
    logger.info("unit testing log");

    Collection<String> contextStack = LoggerContext.getContextStack();
    assert contextStack.contains(testValue);

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    List<String> extraValues = (List<String>) jsonLog.get("extraValues");

    assert extraValues.get(0).equals(testValue);
    assert appender.getMessages().get(0).contains(testValue);
  }

  @Test
  void AddKeyValueToContextTest() {
    LoggerContext.addToContext("testKey", "testValue");
    logger.info("unit testing log");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("testKey").equals("testValue");

    assert LoggerContext.getContextMap().get("testKey").equals("testValue");
    assert appender.getMessages().get(0).contains("\"testKey\":\"testValue\"");
  }

  @Test
  void AddMapEntryToContextTest() {
    Map<String, String> validTestMap = Utils.createStringSampleMap();

    validTestMap.entrySet().forEach(LoggerContext::addToContext);
    Map<String, String> contextMap = LoggerContext.getContextMap();

    assert contextMap.keySet().containsAll(validTestMap.keySet());

    logger.info("unit testing log");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    assert jsonLog.get("uno").equals(1);
    assert jsonLog.get("dos").equals(2);
    assert jsonLog.get("tres").equals(3);
  }

  @Test
  void AddVarArgsToContextMap() {
    assertThrows(IllegalArgumentException.class, () -> LoggerContext.addToContextMap("k1", "v1", "k2"));

    assertThrows(IllegalArgumentException.class, () -> LoggerContext.addToContextMap("k1"));

    LoggerContext.addToContextMap("k1", "v1", "k2", "v2");

    Map<String, String> contextMap = LoggerContext.getContextMap();
    assert contextMap.get("k1").equals("v1");
    assert contextMap.get("k2").equals("v2");

    logger.info("unit testing log");

    List<String> messages = appender.getMessages();
    Map<String, Object> jsonLog = Utils.parseJSON(messages.get(messages.size()-1));
    assert jsonLog.get("k1").equals("v1");
    assert jsonLog.get("k2").equals("v2");
  }

  @SuppressWarnings("unchecked")
  @Test
  void AddVarArgsToContextStack() {
    LoggerContext.addToContextStack("k1", "v1", "k2", "v2");
    Collection<String> contextStack = LoggerContext.getContextStack();

    assert contextStack.contains("k1");
    assert contextStack.contains("v1");
    assert contextStack.contains("k2");
    assert contextStack.contains("v2");

    logger.info("unit testing log");

    Map<String, Object> jsonLog = Utils.parseJSON(appender.getMessages().get(0));
    List<String> extraValues = (List<String>) jsonLog.get("extraValues");

    assert extraValues.contains("k1");
    assert extraValues.contains("v1");
    assert extraValues.contains("k2");
    assert extraValues.contains("v2");
  }
}
