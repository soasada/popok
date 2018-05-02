package com.popokis.popok.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class BodyReader {

  private BodyReader() {}

  public static String asString(InputStream inputStream) {
    StringBuilder stringBuilder = new StringBuilder();

    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    return stringBuilder.toString();
  }
}
