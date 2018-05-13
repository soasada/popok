package com.popokis.popok.util.log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popokis.popok.log.context.LoggerNDC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
  /**
   * Parse a json string into a Map<String , String>
   * @param json string with json
   * @return map with json values
   */
  public static Map<String, Object> parseJSON(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};

      return mapper.readValue(json, typeRef);
    }
    catch (Exception e) {
      throw new RuntimeException("Couldnt parse json:" + json, e);
    }
  }

  /**
   * This function will validate our logs minimum information
   * @param json string in json format to validate
   * @return true if json string has all the info
   */
  @SuppressWarnings("unchecked")
  public static boolean logHasAllInfo(String json) {
    Map<String, Object> jsonLog = parseJSON(json);

    List<Boolean> allChecks = new ArrayList<>() {{
      add(jsonLog.containsKey("level"));
      add(jsonLog.containsKey("logger"));
      add(jsonLog.containsKey("project"));
      add(jsonLog.containsKey("datetime"));
      add(jsonLog.containsKey("msg"));
      add(jsonLog.containsKey("caller"));
    }};

    Map<String, String> callerInfo = (Map<String, String>) jsonLog.get("caller");
    if(callerInfo != null) {
      allChecks.add(callerInfo.containsKey("class"));
      allChecks.add(callerInfo.containsKey("method"));
      allChecks.add(callerInfo.containsKey("file"));
      allChecks.add(callerInfo.containsKey("line"));
    }

    Map<String, String> exceptionInfo = (Map<String, String>) jsonLog.get("exception");
    if(exceptionInfo != null) {
      allChecks.add(exceptionInfo.containsKey("class"));
      allChecks.add(exceptionInfo.containsKey("cause"));
      allChecks.add(exceptionInfo.containsKey("stacktrace"));
    }

    return allChecks.stream().allMatch(c -> c);
  }

  public static Map<String, String> createStringSampleMap() {
    return new HashMap<>() {{
      put("uno", "1");
      put("dos", "2");
      put("tres", "3");
    }};
  }

  public static Map<String, String> createLargeStringSampleMap() {
    Map<String, String> largeMap = createStringSampleMap();
    largeMap.put("cuatro", "4");
    largeMap.put("cinco", "5");

    return largeMap;
  }

  public static Map<String, Integer> createIntegerSampleMap() {
    return new HashMap<>() {{
      put("uno", 1);
      put("dos", 2);
      put("tres", 3);
    }};
  }

  public static List<String> createStringSampleList() {
    return new ArrayList<>() {{
      add("uno");
      add("dos");
      add("tres");
    }};
  }

  public static Set<Integer> createIntegerSampleSet() {
    return new HashSet<>() {{
      add(1);
      add(2);
      add(3);
    }};
  }

  public static Map<String, String> createNDCSampleMap() {
    return new HashMap<>() {{
      put(LoggerNDC.getNDCPrefix() + "1", "value1");
      put(LoggerNDC.getNDCPrefix() + "2", "value2");
      put("3", "value3");
    }};
  }
}
