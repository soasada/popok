package com.popokis.popok.log.context;

import com.popokis.popok.util.log.Utils;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

class LoggerContextOperationTest {

  @Test
  void FilterNDCfromMapTest() {
    Map<String, String> testMap = Utils.createNDCSampleMap();

    Map<String, String> filtered = LoggerContextOperation.filterNDC(testMap);

    assert !filtered.containsKey("3");
    assert filtered.containsKey(LoggerNDC.getNDCPrefix() + "1");
    assert filtered.containsKey(LoggerNDC.getNDCPrefix() + "2");
  }

  @Test
  void FilterMDCfromMapTest() {
    Map<String, String> testMap = Utils.createNDCSampleMap();
    testMap.put("4", "value4");

    Map<String, String> filtered = LoggerContextOperation.filterMDC(testMap);

    assert filtered.containsKey("3");
    assert filtered.containsKey("4");
    assert !filtered.containsKey(LoggerNDC.getNDCPrefix() + "1");
    assert !filtered.containsKey(LoggerNDC.getNDCPrefix() + "2");
  }

  @Test
  void GetNDCListTest() {
    Map<String, String> testMap = Utils.createNDCSampleMap();
    testMap.put("4", "value4");

    Collection<String> ndcList = LoggerContextOperation.getNDCList(testMap);

    assert ndcList.contains("value1");
    assert ndcList.contains("value2");
    assert !ndcList.contains("value3");
    assert !ndcList.contains("value4");
    assert !ndcList.contains("3");
    assert !ndcList.contains("4");
  }
}