package com.popokis.popok.util.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.model.TestModel;

public final class TestModelMapper implements Mapper<TestModel> {
  @Override
  public TestModel map(FixedCachedRowSet source) {
    return TestModel.create(source.getLong("id"), source.getString("name"));
  }
}
