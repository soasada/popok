package com.popokis.popok.util.data;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.query.CompanyRepository;
import com.popokis.popok.util.query.EmployeeRepository;
import com.popokis.popok.util.query.schema.CreateCompanyTableQuery;
import com.popokis.popok.util.query.schema.CreateEmployeeTableQuery;
import com.popokis.popok.util.query.schema.CreateTestTableQuery;
import com.popokis.popok.util.query.schema.DropCompanyTableQuery;
import com.popokis.popok.util.query.schema.DropEmployeeTableQuery;
import com.popokis.popok.util.query.schema.DropTestTableQuery;

public final class DatabaseUtil {

  public static long COMPANY_ID;
  public static long EMPLOYEE_ID;

  private DatabaseUtil() {}

  public static void createTestSchema(Database db) {
    db.executeDML(new CreateTestTableQuery());
    db.executeDML(new CreateCompanyTableQuery());
    db.executeDML(new CreateEmployeeTableQuery());
    loadTestData(db);
  }

  public static void dropTestSchema(Database db) {
    db.executeDML(new DropTestTableQuery());
    db.executeDML(new DropEmployeeTableQuery());
    db.executeDML(new DropCompanyTableQuery());
  }

  private static void loadTestData(Database db) {
    BasicRepository<Company> companyRepository = new CompanyRepository();
    BasicRepository<Employee> employeeRepository = new EmployeeRepository();

    COMPANY_ID = db.executeInsert(companyRepository.saveQuery().insert(Company.create(null, "testCompany")));
    EMPLOYEE_ID = db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee1", COMPANY_ID)));
    db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee2", COMPANY_ID)));
    db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee3", COMPANY_ID)));
  }
}
