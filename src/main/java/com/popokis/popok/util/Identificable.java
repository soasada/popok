package com.popokis.popok.util;

public interface Identificable<T> {
  /**
   * Extract the ID parameter from an object.
   *
   * @param object The object where extract the ID.
   * @return The extracted ID.
   */
  String from(T object);
}
