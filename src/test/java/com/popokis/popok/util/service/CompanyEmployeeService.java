package com.popokis.popok.util.service;

import com.popokis.popok.data.Database;
import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator;
import com.popokis.popok.util.http.CompanyResponse;
import com.popokis.popok.util.query.CompanyEmployeesQuery;

public final class CompanyEmployeeService implements Service<Long, CompanyResponse> {
  @Override
  public CompanyResponse call(Long payload) {
    FixedCachedRowSet fixedCachedRowSet;

    fixedCachedRowSet = Database.getInstance().executeQuery(new CompanyEmployeesQuery(payload));

    if (!fixedCachedRowSet.isBeforeFirst()) {
      throw new RuntimeException("Not found");
    }

    return new CompanyResponseDeserializator().deserialize(fixedCachedRowSet);
  }
}
