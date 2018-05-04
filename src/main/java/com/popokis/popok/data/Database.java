package com.popokis.popok.data;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

  private final HikariConnectionPool connectionPool;

  private Database() {
    connectionPool = HikariConnectionPool.getInstance();
  }

  private static class Holder {
    private static final Database INSTANCE = new Database();
  }

  public static Database getInstance() {
    return Holder.INSTANCE;
  }

  public long executeInsert(Query query) throws SQLException {
    long generatedId;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query(), Statement.RETURN_GENERATED_KEYS)) {
      query.parameters(preparedStatement);
      verifyRows(preparedStatement.executeUpdate());

      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        generatedId = generateId(resultSet);
      }
    }

    return generatedId;
  }

  public int executeDML(Query query) throws SQLException {
    int rowsAffected;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);
      rowsAffected = preparedStatement.executeUpdate();
    }

    return rowsAffected;
  }

  public CachedRowSet executeQuery(Query query) throws SQLException {
    CachedRowSet result;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
        cachedRowSet.populate(resultSet);

        result = cachedRowSet;
      }
    }

    return result;
  }

  private void verifyRows(int affectedRows) {
    if (affectedRows == 0) {
      throw new RuntimeException("No rows affected");
    }
  }

  private long generateId(ResultSet generatedKeys) throws SQLException {
    if (generatedKeys.next()) {
      return generatedKeys.getLong(1);
    } else {
      throw new RuntimeException("DML failed, no ID obtained.");
    }
  }
}
