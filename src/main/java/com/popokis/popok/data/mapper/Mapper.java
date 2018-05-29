package com.popokis.popok.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;

public interface Mapper<T> {
  T map(FixedCachedRowSet rowSet);
}
