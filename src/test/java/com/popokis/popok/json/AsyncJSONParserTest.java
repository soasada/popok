/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popokis.popok.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AsyncJSONParserTest {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  private static final JsonFactory JSON_FACTORY = new JsonFactory();

  @Test
  void shouldParseWholeJson() throws IOException {
    Model expected = new Model("TEST", LocalDateTime.parse("2019-08-11T21:33:20.989957"), List.of(1, 2, 3));
    String wholeJsonModel = "{\"id\":\"TEST\",\"timestamp\":\"2019-08-11T21:33:20.989957\",\"values\":[1,2,3]}";

    JsonParser nonBlockingByteArrayParser = JSON_FACTORY.createNonBlockingByteArrayParser();
    AsyncJSONParser asyncJSONParser = new AsyncJSONParser(nonBlockingByteArrayParser, MAPPER, true);
    List<TokenBuffer> tokens = asyncJSONParser.whole(wholeJsonModel.getBytes());
    Model actual = MAPPER.readValue(tokens.get(0).asParser(), Model.class);
    asyncJSONParser.endOfInput();
    nonBlockingByteArrayParser.close();

    assertEquals(expected, actual);
  }

  @Test
  void shouldParseJsonChunks() throws IOException {
    Model expected = new Model("TEST", LocalDateTime.parse("2019-08-11T21:33:20.989957"), List.of(1, 2, 3));
    List<String> jsonChunks = List.of(
        "{",
        "\"id\":\"TEST\",",
        "\"timestamp\":\"2019-08-11T21:33:20.989957\",",
        "\"values\":[1,2",
        ",3]",
        "}"
    );
    JsonParser nonBlockingByteArrayParser = JSON_FACTORY.createNonBlockingByteArrayParser();
    AsyncJSONParser asyncJSONParser = new AsyncJSONParser(nonBlockingByteArrayParser, MAPPER, true);
    List<byte[]> bytesChunks = jsonChunks.stream().map(String::getBytes).collect(toList());
    List<TokenBuffer> tokens = asyncJSONParser.chunks(bytesChunks);
    Model actual = MAPPER.readValue(tokens.get(0).asParser(), Model.class);
    asyncJSONParser.endOfInput();
    nonBlockingByteArrayParser.close();

    assertEquals(expected, actual);
  }

  @EqualsAndHashCode
  private static final class Model {
    private String id;
    private LocalDateTime timestamp;
    private List<Integer> values;

    public Model() {}

    public Model(String id, LocalDateTime timestamp, List<Integer> values) {
      this.id = id;
      this.timestamp = timestamp;
      this.values = values;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public LocalDateTime getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
    }

    public List<Integer> getValues() {
      return values;
    }

    public void setValues(List<Integer> values) {
      this.values = values;
    }
  }
}