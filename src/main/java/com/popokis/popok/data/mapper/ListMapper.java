package com.popokis.popok.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;

import java.util.ArrayList;
import java.util.List;

public final class ListMapper<T> implements Mapper<List<T>> {

  private final Mapper<T> modelMapper;

  public ListMapper(Mapper<T> modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public List<T> map(FixedCachedRowSet rowSet) {
    List<T> resultList = new ArrayList<>();

    while (rowSet.next()) {
      resultList.add(modelMapper.map(rowSet));
    }

    return resultList;
  }
}
