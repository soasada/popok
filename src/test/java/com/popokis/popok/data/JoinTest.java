package com.popokis.popok.data;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.access.FixedCachedRowSet;
import com.popokis.popok.data.access.HikariConnectionPool;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.util.data.BootstrapDatabase;
import com.popokis.popok.util.data.mapper.CompanyResponseMapper;
import com.popokis.popok.util.data.mapper.CompanyResponseMapper2;
import com.popokis.popok.util.data.mapper.EmployeeResponseMapper;
import com.popokis.popok.util.http.CompanyResponse;
import com.popokis.popok.util.http.EmployeeResponse;
import com.popokis.popok.util.query.CompanyEmployeesAliasQuery;
import com.popokis.popok.util.query.CompanyEmployeesQuery;
import com.popokis.popok.util.query.EmployeeCompanyQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JoinTest {

  private static Database db;

  @BeforeAll
  static void initAll() {
    db = new Database(HikariConnectionPool.getInstance());
    BootstrapDatabase.createTestSchema(db);
  }

  @Test
  void getAllEmployeesForCompanyTest() {
    FixedCachedRowSet rowSet = db.executeQuery(new CompanyEmployeesQuery(BootstrapDatabase.COMPANY_ID));
    rowSet.next();

    Mapper<CompanyResponse> deserializator = new CompanyResponseMapper();

    CompanyResponse companyResponse = deserializator.map(rowSet);

    assertEquals(3, companyResponse.employees().size());
  }

  @Test
  void getEmployeeWithCompanyTest() {
    FixedCachedRowSet rowSet = db.executeQuery(new EmployeeCompanyQuery(BootstrapDatabase.EMPLOYEE_ID));
    rowSet.next();

    Mapper<EmployeeResponse> deserializator = new EmployeeResponseMapper();

    EmployeeResponse employeeResponse = deserializator.map(rowSet);

    assertEquals("testCompany", employeeResponse.company().name());
  }

  @Test
  void getAllEmployeesForCompanyWithAliasesTest() {
    FixedCachedRowSet rowSet = db.executeQuery(new CompanyEmployeesAliasQuery(BootstrapDatabase.COMPANY_ID));
    rowSet.next();

    Mapper<CompanyResponse> deserializator = new CompanyResponseMapper2();

    CompanyResponse companyResponse = deserializator.map(rowSet);

    assertEquals(3, companyResponse.employees().size());
  }

  @AfterAll
  static void tearDownAll() {
    BootstrapDatabase.dropTestSchema(db);
    db = null;
  }
}
