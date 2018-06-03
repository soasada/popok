package com.popokis.popok.util.data;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.query.CompanyRepository;
import com.popokis.popok.util.query.EmployeeRepository;
import com.popokis.popok.util.query.TestingQueryFactory;

import static com.popokis.popok.util.data.FakeData.TEST_DELETE_ID;
import static com.popokis.popok.util.data.FakeData.TEST_FIND_ID;
import static com.popokis.popok.util.data.FakeData.TEST_MODIFY_ID;

public final class BootstrapDatabase {

  public static long COMPANY_ID;
  public static long EMPLOYEE_ID;

  private BootstrapDatabase() {}

  public static void createTestSchema(Database db) {
    db.executeDML(TestingQueryFactory.create("CREATE TABLE IF NOT EXISTS test (id INT UNSIGNED NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY (id))"));
    db.executeDML(TestingQueryFactory.create("CREATE TABLE IF NOT EXISTS company (c_id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
        "c_name VARCHAR(255) NOT NULL, PRIMARY KEY (c_id))"));
    db.executeDML(TestingQueryFactory.create("CREATE TABLE IF NOT EXISTS employee (e_id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
        "e_name VARCHAR(255) NOT NULL, e_company_id INT UNSIGNED NOT NULL, PRIMARY KEY (e_id), " +
        "INDEX fk_employee_company_idx (e_company_id ASC), " +
        "CONSTRAINT fk_employee_company " +
        "FOREIGN KEY (e_company_id) " +
        "REFERENCES company (c_id) " +
        "ON DELETE NO ACTION " +
        "ON UPDATE NO ACTION)"));

    loadTestData(db);
    loadCompanyEmployeeData(db);
  }

  public static void dropTestSchema(Database db) {
    db.executeDML(TestingQueryFactory.create("DROP TABLE IF EXISTS test"));
    db.executeDML(TestingQueryFactory.create("DROP TABLE IF EXISTS employee"));
    db.executeDML(TestingQueryFactory.create("DROP TABLE IF EXISTS company"));
  }

  private static void loadTestData(Database db) {
    db.executeInsert(TestingQueryFactory.create("INSERT INTO test (id, name) VALUES (" + TEST_FIND_ID + ", 'findTest')"));
    db.executeInsert(TestingQueryFactory.create("INSERT INTO test (id, name) VALUES (" + TEST_DELETE_ID + ", 'deleteTest')"));
    db.executeInsert(TestingQueryFactory.create("INSERT INTO test (id, name) VALUES (" + TEST_MODIFY_ID + ", 'modifyTest')"));
  }

  private static void loadCompanyEmployeeData(Database db) {
    BasicRepository<Company> companyRepository = new CompanyRepository();
    BasicRepository<Employee> employeeRepository = new EmployeeRepository();

    COMPANY_ID = db.executeInsert(companyRepository.saveQuery().insert(Company.create(null, "testCompany")));
    EMPLOYEE_ID = db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee1", COMPANY_ID)));
    db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee2", COMPANY_ID)));
    db.executeInsert(employeeRepository.saveQuery().insert(Employee.create(null, "testEmployee3", COMPANY_ID)));
  }
}
