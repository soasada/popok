package com.popokis.popok.http.manipulator;

public abstract class ManipulatorDecorator<P> implements Manipulator<P> {

  private final Manipulator<P> manipulator;

  public ManipulatorDecorator(Manipulator<P> manipulator) {
    this.manipulator = manipulator;
  }

  @Override
  public P manipulate(P payload) {
    return manipulator.manipulate(payload);
  }
}
