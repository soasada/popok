package com.popokis.popok.util;

/**
 * All the requests in a public API must implement this interface in order to identify them. This help to the process
 * of the log and track the request and response.
 */
public interface Identifiable {
  String id();
}
