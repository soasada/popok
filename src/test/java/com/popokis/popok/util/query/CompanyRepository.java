package com.popokis.popok.util.query;

import com.popokis.popok.data.BasicRepository;
import com.popokis.popok.data.Query;
import com.popokis.popok.util.data.model.Company;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class CompanyRepository implements BasicRepository<Company> {
  @Override
  public Query save(Company company) {
    return new Query() {
      @Override
      public String query() {
        return "INSERT INTO company (name) VALUES(?)";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, company.name());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    };
  }

  @Override
  public Query modify(Company company) {
    return new Query() {
      @Override
      public String query() {
        return "UPDATE company SET name = ? WHERE id = ?";
      }

      @Override
      public void parameters(PreparedStatement stm) {
        try {
          stm.setString(1, company.name());
          stm.setLong(2, company.id());
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
        return "SELECT * FROM company WHERE id = ?";
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
        return "DELETE FROM company WHERE id = ? LIMIT 1";
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
        return "SELECT * FROM company";
      }

      @Override
      public void parameters(PreparedStatement stm) {

      }
    };
  }
}
