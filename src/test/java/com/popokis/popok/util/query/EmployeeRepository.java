package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;
import com.popokis.popok.util.data.model.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class EmployeeRepository implements BasicRepository<Employee> {
  @Override
  public Query save(Employee employee) {
    return new Query() {
      @Override
      public String query() {
        return "INSERT INTO employee (name, company_id) VALUES(?, ?)";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, employee.name());
          stm.setLong(2, employee.companyId());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query modify(Employee employee) {
    return new Query() {
      @Override
      public String query() {
        return "UPDATE employee SET name = ?, company_id = ? WHERE id = ?";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, employee.name());
          stm.setLong(2, employee.companyId());
          stm.setLong(3, employee.id());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query find(long id) {
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM employee WHERE id = ?";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setLong(1, id);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query remove(long id) {
    return new Query() {
      @Override
      public String query() {
        return "DELETE FROM employee WHERE id = ? LIMIT 1";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setLong(1, id);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query all() {
    return new Query() {
      @Override
      public String query() {
        return "SELECT * FROM employee";
      }

      @Override
      public void parameters(PreparedStatement stm) {

      }
    };
  }
}
