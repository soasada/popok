package com.popokis.popok.util.data.deserializator;

import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.model.Company;

public final class CompanyDeserializator implements Deserializator<Company, FixedCachedRowSet> {
  @Override
  public Company deserialize(FixedCachedRowSet source) {
    return Company.create(source.getLong("c_id"), source.getString("c_name"));
  }
}
