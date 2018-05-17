package com.popokis.popok.util.data.model;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class TestModel {

  public static TestModel create(@Nullable Long id, String name) {
    return new AutoValue_TestModel(id, name);
  }

  @Nullable
  public abstract Long id();

  public abstract String name();
}
