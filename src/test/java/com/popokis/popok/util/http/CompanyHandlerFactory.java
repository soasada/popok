package com.popokis.popok.util.http;

import com.popokis.popok.data.Database;
import com.popokis.popok.data.FixedCachedRowSet;
import com.popokis.popok.http.extractor.IdExtractor;
import com.popokis.popok.http.handler.BasicHandlerFactory;
import com.popokis.popok.http.handler.SyncHandler;
import com.popokis.popok.http.manipulator.BasicManipulator;
import com.popokis.popok.util.data.deserializator.CompanyDeserializator;
import com.popokis.popok.util.data.deserializator.CompanyResponseDeserializator;
import com.popokis.popok.util.data.deserializator.EmployeeDeserializator;
import com.popokis.popok.util.data.model.Company;
import com.popokis.popok.util.data.model.Employee;
import com.popokis.popok.util.query.CompanyEmployeesQuery;
import com.popokis.popok.util.query.CompanyRepository;
import com.popokis.popok.util.query.EmployeeRepository;
import com.popokis.popok.util.validator.BasicValidator;
import com.popokis.popok.util.validator.IdValidator;
import io.undertow.server.HttpHandler;

public final class CompanyHandlerFactory {

  private CompanyHandlerFactory() {}

  public static BasicHandlerFactory<Company> crudCompany() {
    return new BasicHandlerFactory<>(
        new BasicValidator<>(),
        new BasicValidator<>(),
        "company",
        Company.class,
        new CompanyRepository(),
        new CompanyDeserializator());
  }

  public static BasicHandlerFactory<Employee> crudEmployee() {
    return new BasicHandlerFactory<>(
        new BasicValidator<>(),
        new BasicValidator<>(),
        "company",
        Employee.class,
        new EmployeeRepository(),
        new EmployeeDeserializator());
  }

  public static HttpHandler companyEmployee() {
    return new SyncHandler<>(
        new IdExtractor(),
        "company",
        Long::parseLong,
        new IdValidator(new BasicValidator<>()),
        new BasicManipulator<>(),
        payload -> {
          FixedCachedRowSet fixedCachedRowSet;

          fixedCachedRowSet = Database.getInstance().executeQuery(new CompanyEmployeesQuery(payload));

          if (!fixedCachedRowSet.isBeforeFirst()) {
            throw new RuntimeException("Not found");
          }

          return new CompanyResponseDeserializator().deserialize(fixedCachedRowSet);
        },
        object -> "",
        new BasicManipulator<>()
    );
  }
}
