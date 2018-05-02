package com.popokis.popok.http.manipulator;

public final class BasicManipulator<P> implements Manipulator<P> {
  @Override
  public P manipulate(P payload) {
    return payload;
  }
}
