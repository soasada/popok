package com.popokis.popok.serialization.json;

import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.json.CustomObjectMapper;

import java.io.IOException;

public final class JacksonDeserializator<T> implements Deserializator<T, String> {

  private final CustomObjectMapper mapper;
  private final Class<T> sourceType;

  public JacksonDeserializator(Class<T> sourceType) {
    mapper = CustomObjectMapper.getInstance();
    this.sourceType = sourceType;
  }

  @Override
  public T deserialize(String source) {
    try {
      return mapper.mapper().readValue(source, sourceType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
