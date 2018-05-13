package com.popokis.popok.data;

import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator2;
import com.popokis.popok.util.data.deserializator.EmployeeResponseDeserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.http.CompanyResponse;
import com.popokis.popok.util.http.EmployeeResponse;
import com.popokis.popok.util.query.CompanyEmployeesAliasQuery;
import com.popokis.popok.util.query.CompanyEmployeesQuery;
import com.popokis.popok.util.query.CompanyRepository;
import com.popokis.popok.util.query.EmployeeCompanyQuery;
import com.popokis.popok.util.query.EmployeeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JoinTest {

  private static BasicRepository<Company> companyRepository;
  private static BasicRepository<Employee> employeeRepository;

  @BeforeAll
  static void initAll() throws SQLException {
    companyRepository = new CompanyRepository();
    employeeRepository = new EmployeeRepository();
    DatabaseUtil.createTestSchema();
  }

  @Test
  void getAllEmployeesForCompanyTest() throws SQLException {
    long companyId = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany")));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee1", companyId)));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee2", companyId)));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee3", companyId)));

    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new CompanyEmployeesQuery(companyId));
    rowSet.next();

    Deserializator<CompanyResponse, FixedCachedRowSet> deserializator = new CompanyResponseDeserializator();

    CompanyResponse companyResponse = deserializator.deserialize(rowSet);

    assertEquals(3, companyResponse.employees().size());
  }

  @Test
  void getEmployeeWithCompanyTest() throws SQLException {
    long companyId = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany2")));
    long employeeId = Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee4", companyId)));

    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new EmployeeCompanyQuery(employeeId));
    rowSet.next();

    Deserializator<EmployeeResponse, FixedCachedRowSet> deserializator = new EmployeeResponseDeserializator();

    EmployeeResponse employeeResponse = deserializator.deserialize(rowSet);

    assertEquals("testCompany2", employeeResponse.company().name());
  }

  @Test
  void getAllEmployeesForCompanyWithAliasesTest() throws SQLException {
    long companyId = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany3")));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee5", companyId)));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee6", companyId)));

    FixedCachedRowSet rowSet = Database.getInstance().executeQuery(new CompanyEmployeesAliasQuery(companyId));
    rowSet.next();

    Deserializator<CompanyResponse, FixedCachedRowSet> deserializator = new CompanyResponseDeserializator2();

    CompanyResponse companyResponse = deserializator.deserialize(rowSet);

    assertEquals(2, companyResponse.employees().size());
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    companyRepository = null;
    employeeRepository = null;
    DatabaseUtil.dropTestSchema();
  }
}
