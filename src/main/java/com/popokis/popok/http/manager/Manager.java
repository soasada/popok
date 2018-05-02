package com.popokis.popok.http.manager;

/**
 * The manager is in charge of execute a chain of steps manipulating the requests and responses of the web services.
 *
 * @param <R> Request type.
 * @param <S> Response type.
 */
public interface Manager<R, S> {
  S manage(R request);
}
