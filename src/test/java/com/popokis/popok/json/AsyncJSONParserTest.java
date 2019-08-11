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
import java.util.UUID;

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

  @EqualsAndHashCode
  private static final class Model {
    private String id;
    private LocalDateTime timestamp;
    private List<Integer> values;

    public Model() {
    }

    public Model(String id, LocalDateTime timestamp, List<Integer> values) {
      this.id = id;
      this.timestamp = timestamp;
      this.values = values;
    }

    public static Model random() {
      Model model = new Model();
      model.setId(UUID.randomUUID().toString());
      model.setTimestamp(LocalDateTime.now());
      model.setValues(List.of(1, 2, 3));
      return model;
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