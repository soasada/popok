package com.popokis.popok.log.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

public final class LoggerUtils {

  private LoggerUtils() {}

  public static Optional<JsonNode> parseJSON(String json) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      return Optional.ofNullable(objectMapper.readTree(json));
    } catch (IOException ignored) {}

    return Optional.empty();
  }

  /**
   * Will use jackson to validate if json string is a valid
   * json format
   * @param json string in json format
   * @return true if valid json, false otherwise
   */
  public static boolean isValidJSON(String json) {
    return LoggerUtils.parseJSON(json).isPresent();
  }
}
