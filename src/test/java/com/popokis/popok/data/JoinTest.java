package com.popokis.popok.data;

import com.popokis.popok.serialization.Deserializator;
import com.popokis.popok.serialization.db.ListDeserializator;
import com.popokis.popok.util.data.DatabaseUtil;
import com.popokis.popok.util.data.deserializator.TestModelDeserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.data.model.TestModel;
import com.popokis.popok.util.query.CompanyRepository;
import com.popokis.popok.util.query.EmployeeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
  void JoinCompanyWithEmployeeTest() throws SQLException {
    long companyId = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany", null)));
  }

  @Test
  void JoinCompanyWithEmployeeAliasesTest() throws SQLException {
    long companyId = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany", null)));
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    companyRepository = null;
    employeeRepository = null;
    DatabaseUtil.dropTestSchema();
  }
}
