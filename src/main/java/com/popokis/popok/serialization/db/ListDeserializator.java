package com.popokis.popok.serialization.db;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;

import java.util.ArrayList;
import java.util.List;

public final class ListDeserializator<T> implements Deserializator<List<T>, FixedCachedRowSet> {

  private final Deserializator<T, FixedCachedRowSet> modelDeserializator;

  public ListDeserializator(Deserializator<T, FixedCachedRowSet> modelDeserializator) {
    this.modelDeserializator = modelDeserializator;
  }

  @Override
  public List<T> deserialize(FixedCachedRowSet rowSet) {
    List<T> resultList = new ArrayList<>();

    while (rowSet.next()) {
      resultList.add(modelDeserializator.deserialize(rowSet));
    }

    return resultList;
  }
}
