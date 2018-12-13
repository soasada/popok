package com.popokis.popok.service;

/**
 * A service is an external resource, such as databases or web services. You must implement this interface when
 * your application interacts with some external resource.
 *
 * @param <P> Payload of the service.
 * @param <T> Data model returned by the service.
 */
public interface Service<P, T> {
  T call(P payload);
}
