package com.popokis.popok.http.make_request;

public interface MakeRequest<S> {
  /**
   * Performs a request to an HTTP service.
   *
   * @param uri The url of the service without query string.
   * @param payload The String request payload representation.
   * @return The service response.
   */
  S to(String uri, String payload);
}
