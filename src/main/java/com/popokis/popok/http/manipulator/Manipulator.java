package com.popokis.popok.http.manipulator;

public interface Manipulator<P> {
  /**
   * Performs an action with an object payload (request or response).
   *
   * @param payload A valid payload.
   * @return A manipulated payload, for example, a request with the id parameter changed or a response with a sorted list.
   */
  P manipulate(P payload);
}
