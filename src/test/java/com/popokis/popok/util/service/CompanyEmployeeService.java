package com.popokis.popok.util.service;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.access.HikariConnectionPool;
import com.popokis.popok.service.Service;
import com.popokis.popok.util.data.mapper.CompanyResponseMapper;
import com.popokis.popok.util.http.CompanyResponse;
import com.popokis.popok.util.query.CompanyEmployeesQuery;

public final class CompanyEmployeeService implements Service<Long, CompanyResponse> {
  @Override
  public CompanyResponse call(Long payload) {
    FixedCachedRowSet fixedCachedRowSet;
    Database db = new Database(HikariConnectionPool.getInstance());

    fixedCachedRowSet = db.executeQuery(new CompanyEmployeesQuery(payload));

    if (!fixedCachedRowSet.isBeforeFirst()) {
      throw new RuntimeException("Not found");
    }

    return new CompanyResponseMapper().map(fixedCachedRowSet);
  }
}
