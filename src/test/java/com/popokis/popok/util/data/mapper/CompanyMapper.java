package com.popokis.popok.util.data.mapper;

import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.model.Company;

public final class CompanyMapper implements Mapper<Company> {
  @Override
  public Company map(FixedCachedRowSet source) {
    return Company.create(source.getLong("c_id"), source.getString("c_name"));
  }
}
