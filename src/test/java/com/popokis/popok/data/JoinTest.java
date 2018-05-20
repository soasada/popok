package com.popokis.popok.data;

import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator2;
import com.popokis.popok.util.data.deserializator.EmployeeResponseDeserializator;
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

  @BeforeAll
  static void initAll() {
    DatabaseUtil.createTestSchema();
  }

  @Test
  void getAllEmployeesForCompanyTest() {
    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new CompanyEmployeesQuery(DatabaseUtil.COMPANY_ID));
    rowSet.next();

    Deserializator<CompanyResponse, FixedCachedRowSet> deserializator = new CompanyResponseDeserializator();

    CompanyResponse companyResponse = deserializator.deserialize(rowSet);

    assertEquals(3, companyResponse.employees().size());
  }

  @Test
  void getEmployeeWithCompanyTest() {
    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new EmployeeCompanyQuery(DatabaseUtil.EMPLOYEE_ID));
    rowSet.next();

    Deserializator<EmployeeResponse, FixedCachedRowSet> deserializator = new EmployeeResponseDeserializator();

    EmployeeResponse employeeResponse = deserializator.deserialize(rowSet);

    assertEquals("testCompany", employeeResponse.company().name());
  }

  @Test
  void getAllEmployeesForCompanyWithAliasesTest() {
    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new CompanyEmployeesAliasQuery(DatabaseUtil.COMPANY_ID));
    rowSet.next();

    Deserializator<CompanyResponse, FixedCachedRowSet> deserializator = new CompanyResponseDeserializator2();

    CompanyResponse companyResponse = deserializator.deserialize(rowSet);

    assertEquals(3, companyResponse.employees().size());
  }

  @AfterAll
  static void tearDownAll() {
    DatabaseUtil.dropTestSchema();
  }
}
