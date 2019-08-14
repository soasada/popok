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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.async.ByteArrayFeeder;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * This class was originally created by the spring framework team.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @author Nicolás Vargas
 */

public final class AsyncJSONParser {

  private final JsonParser parser;
  private final DeserializationContext deserializationContext;
  private final boolean tokenizeArrayElements;
  private final ByteArrayFeeder feeder;

  private TokenBuffer tokenBuffer;
  private int objectDepth;
  private int arrayDepth;

  public AsyncJSONParser(JsonParser parser, ObjectMapper mapper, boolean tokenizeArrayElements) {
    this.parser = parser;
    this.deserializationContext = mapper.getDeserializationContext();
    this.tokenizeArrayElements = tokenizeArrayElements;
    this.feeder = (ByteArrayFeeder) parser.getNonBlockingInputFeeder();
    this.tokenBuffer = new TokenBuffer(parser, mapper.getDeserializationContext());
  }

  public List<TokenBuffer> whole(byte[] data) {
    return parse(data, new ArrayList<>());
  }

  public List<TokenBuffer> chunks(List<byte[]> data) {
    return data.stream()
        .map(d -> parse(d, new ArrayList<>()))
        .flatMap(List::stream)
        .collect(toList());
  }

  public void endOfInput() {
    feeder.endOfInput();
  }

  private List<TokenBuffer> parse(byte[] data, List<TokenBuffer> result) {
    try {
      if (!feeder.needMoreInput()) throw new IOException("Got NOT_AVAILABLE, could not feed more input");
      feeder.feedInput(data, 0, data.length);

      while (true) {
        JsonToken token = parser.nextToken();
        if (token == JsonToken.NOT_AVAILABLE) break;
        updateDepth(token);
        if (tokenizeArrayElements) {
          processTokenArray(token, result);
        } else {
          processTokenNormal(token, result);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new ArrayList<>(result);
  }

  private void updateDepth(JsonToken token) {
    switch (token) {
      case START_OBJECT:
        objectDepth++;
        break;
      case END_OBJECT:
        objectDepth--;
        break;
      case START_ARRAY:
        arrayDepth++;
        break;
      case END_ARRAY:
        arrayDepth--;
        break;
    }
  }

  private void processTokenNormal(JsonToken token, List<TokenBuffer> result) throws IOException {
    tokenBuffer.copyCurrentEvent(parser);

    if ((token.isStructEnd() || token.isScalarValue()) && objectDepth == 0 && arrayDepth == 0) {
      result.add(tokenBuffer);
      tokenBuffer = new TokenBuffer(parser, deserializationContext);
    }
  }

  private void processTokenArray(JsonToken token, List<TokenBuffer> result) throws IOException {
    if (!isTopLevelArrayToken(token)) {
      tokenBuffer.copyCurrentEvent(parser);
    }

    if (objectDepth == 0 && (arrayDepth == 0 || arrayDepth == 1) &&
        (token == JsonToken.END_OBJECT || token.isScalarValue())) {
      result.add(tokenBuffer);
      tokenBuffer = new TokenBuffer(parser, deserializationContext);
    }
  }

  private boolean isTopLevelArrayToken(JsonToken token) {
    return objectDepth == 0 && ((token == JsonToken.START_ARRAY && arrayDepth == 1) ||
        (token == JsonToken.END_ARRAY && arrayDepth == 0));
  }
}
