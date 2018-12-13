package com.popokis.popok.data.access;

import com.popokis.popok.data.query.Query;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Allow to execute Query objects directly with the database. Use a connection pool by default for
 * connection reuse and performance improvement.
 *
 * @since 0.1.0
 */
public final class Database<T> {

  private final ConnectionPool<T> connectionPool;

  public Database(ConnectionPool<T> connectionPool) {
    this.connectionPool = connectionPool;
  }

  public long executeInsert(Query query) {
    long generatedId;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query(), Statement.RETURN_GENERATED_KEYS)) {
      query.parameters(preparedStatement);
      verifyRows(preparedStatement.executeUpdate());

      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        generatedId = generateId(resultSet);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return generatedId;
  }

  public int executeDML(Query query) {
    int rowsAffected;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);
      rowsAffected = preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return rowsAffected;
  }

  public FixedCachedRowSet executeQuery(Query query) {
    FixedCachedRowSet result;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
        cachedRowSet.populate(resultSet);

        result = new FixedCachedRowSet(cachedRowSet);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
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
