package com.popokis.popok.data.query;

public final class QueryGenerator {

  private final String columnPrefix;

  public QueryGenerator(String columnPrefix) {
    this.columnPrefix = columnPrefix;
  }

  public String columnPrefix() {
    return columnPrefix;
  }

  public String putPrefix(String... columns) {
    String commaSpace = ", ";
    String space = " ";

    return generateStringWithPrefixWithoutFinal(columnPrefix, commaSpace, space, columns);
  }

  public String putQuestionMark(String... columns) {
    return generateStringWithPrefixWithoutFinal(columnPrefix, " = ?, ", " = ? ", columns);
  }

  private String generateStringWithPrefixWithoutFinal(String first, String lastNormal, String last, String... elements) {
    StringBuilder stringBuilder = new StringBuilder();

    for (int i = 0; i < elements.length; i++) {
      if (i < elements.length - 1) {
        stringBuilder.append(first).append(elements[i]).append(lastNormal);
      } else {
        stringBuilder.append(first).append(elements[i]).append(last);
      }
    }

    return stringBuilder.toString();
  }
}
