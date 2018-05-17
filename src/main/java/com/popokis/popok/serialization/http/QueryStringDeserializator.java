package com.popokis.popok.serialization.http;

import com.popokis.popok.serialization.Deserializator;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public final class QueryStringDeserializator implements Deserializator<Map<String, List<String>>, String> {

  @Override
  public Map<String, List<String>> deserialize(String queryString) {
    if (Objects.isNull(queryString) || queryString.isEmpty()) {
      return Collections.emptyMap();
    }

    return Arrays.stream(queryString.split("&"))
        .map(this::splitQueryParameter)
        .collect(groupingBy(Map.Entry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
  }

  private Map.Entry<String, String> splitQueryParameter(String param) {
    final int equalIdx = param.indexOf("=");
    final String key = equalIdx > 0 ? param.substring(0, equalIdx) : param;
    final String value = equalIdx > 0 && param.length() > equalIdx + 1 ? param.substring(equalIdx + 1) : null;
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }
}
