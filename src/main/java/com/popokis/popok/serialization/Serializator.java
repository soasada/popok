package com.popokis.popok.serialization;

/**
 * Serialize a java object to his "message/source" format representation (normally JSON or QueryString).
 *
 * @param <S> Format object representation.
 * @param <T> Java object to serialize.
 */
public interface Serializator<S, T> {
  /**
   * Converts a java objects to a message representation. A message representation is a valid format
   * that a service understand.
   *
   * @param javaObject Object to serialize.
   * @return A well formed message representation of the serialized object.
   */
  S serialize(T javaObject);
}
