package com.popokis.popok.serialization.http;

import com.fasterxml.jackson.databind.JavaType;
import com.popokis.popok.http.response.RestResponse;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.json.CustomObjectMapper;

import java.io.IOException;

public final class RestResponseDeserializator<S> implements Deserializator<RestResponse<S>, String> {

  private final CustomObjectMapper mapper;
  private final Class<S> type;

  public RestResponseDeserializator(Class<S> type) {
    this.mapper = CustomObjectMapper.getInstance();
    this.type = type;
  }

  @Override
  public RestResponse<S> deserialize(String source) {
    try {
      JavaType javaType = mapper.mapper().getTypeFactory().constructParametricType(RestResponse.class, type);
      return mapper.mapper().readValue(source, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
