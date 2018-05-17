package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.model.TestModel;

public final class TestModelDeserializator implements Deserializator<TestModel, FixedCachedRowSet> {
  @Override
  public TestModel deserialize(FixedCachedRowSet source) {
    return TestModel.create(source.getLong("id"), source.getString("name"));
  }
}
