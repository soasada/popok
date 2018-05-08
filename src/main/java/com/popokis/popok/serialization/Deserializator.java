package com.popokis.popok.serialization;

/**
 * Deserialize a source data to a java object.
 *
 * @param <T> Desired data object.
 * @param <S> Source from parse the data object.
 */
public interface Deserializator<T, S> {
  /**
   * Converts a source data to a java object.
   *
   * @param source The source from which to extract the data.
   * @return Desired data object.
   */
  T deserialize(S source);
}
