package com.popokis.popok.service;

public abstract class ServiceDecorator<P, T> implements Service<P, T> {

  private final Service<P, T> service;

  protected ServiceDecorator(Service<P, T> service) {
    this.service = service;
  }

  @Override
  public T call(P payload) {
    return service.call(payload);
  }
}
