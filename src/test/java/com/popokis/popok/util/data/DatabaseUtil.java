package com.popokis.popok.util.data;

import com.popokis.popok.data.Database;
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

  public static void createTestSchema() {
    Database.getInstance().executeDML(new CreateTestTableQuery());
    Database.getInstance().executeDML(new CreateCompanyTableQuery());
    Database.getInstance().executeDML(new CreateEmployeeTableQuery());
    loadTestData();
  }

  public static void dropTestSchema() {
    Database.getInstance().executeDML(new DropTestTableQuery());
    Database.getInstance().executeDML(new DropEmployeeTableQuery());
    Database.getInstance().executeDML(new DropCompanyTableQuery());
  }

  private static void loadTestData() {
    BasicRepository<Company> companyRepository = new CompanyRepository();
    BasicRepository<Employee> employeeRepository = new EmployeeRepository();

    COMPANY_ID = Database.getInstance().executeInsert(companyRepository.save(Company.create(null, "testCompany")));
    EMPLOYEE_ID = Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee1", COMPANY_ID)));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee2", COMPANY_ID)));
    Database.getInstance().executeInsert(employeeRepository.save(Employee.create(null, "testEmployee3", COMPANY_ID)));
  }
}
