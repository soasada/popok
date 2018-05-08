package com.popokis.popok.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;

public final class JacksonSerializator<T> implements Serializator<String, T> {

  private final CustomObjectMapper mapper;

  public JacksonSerializator() {
    mapper = CustomObjectMapper.getInstance();
  }

  @Override
  public String serialize(T javaObject) {
    try {
      return mapper.mapper().writeValueAsString(javaObject);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
