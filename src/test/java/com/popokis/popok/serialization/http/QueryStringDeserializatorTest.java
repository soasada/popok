package com.popokis.popok.serialization.http;

import com.popokis.popok.serialization.Deserializator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QueryStringDeserializatorTest {

  private static Deserializator<Map<String, List<String>>, String> deserializator;

  @BeforeAll
  static void initAll() {
    deserializator = new QueryStringDeserializator();
  }

  @Test
  void normalQueryStringTest() {
    String normalQueryString = "param=test1&param2=test2";

    Map<String, List<String>> queryStringMap = deserializator.deserialize(normalQueryString);

    assertEquals("test1", queryStringMap.get("param").get(0));
    assertEquals("test2", queryStringMap.get("param2").get(0));
  }

  @Test
  void moreThanOneParamTest() {
    String normalQueryString = "param=test1&param2=test2&param2=test3";

    Map<String, List<String>> queryStringMap = deserializator.deserialize(normalQueryString);

    assertEquals("test2", queryStringMap.get("param2").get(0));
    assertEquals("test3", queryStringMap.get("param2").get(1));
  }

  @Test
  void moreThanOneParamWithEqualNullTest() {
    String normalQueryString = "param=test1&param2=test2&param2=";

    Map<String, List<String>> queryStringMap = deserializator.deserialize(normalQueryString);

    assertEquals("test2", queryStringMap.get("param2").get(0));
    assertNull(queryStringMap.get("param2").get(1));
  }

  @Test
  void moreThanOneParamNullTest() {
    String normalQueryString = "param=test1&param2=test2&param2";

    Map<String, List<String>> queryStringMap = deserializator.deserialize(normalQueryString);

    assertEquals("test2", queryStringMap.get("param2").get(0));
    assertNull(queryStringMap.get("param2").get(1));
  }

  @AfterAll
  static void tearDownAll() {
    deserializator = null;
  }
}