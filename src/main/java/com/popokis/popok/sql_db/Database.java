/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popokis.popok.sql_db;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public final class Database {

  private final DataSource connectionPool;

  private Database(DataSource connectionPool) {
    this.connectionPool = connectionPool;
  }

  public static Database create(DataSource dataSource) {
    return new Database(dataSource);
  }

  public long executeInsert(Query query) {
    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query(), Statement.RETURN_GENERATED_KEYS)) {
      query.parameters(preparedStatement);

      if (preparedStatement.executeUpdate() == 0) throw new RuntimeException("No rows affected");

      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        return generateId(resultSet);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int executeDML(Query query) {
    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);
      return preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> Optional<T> executeQuery(Query query, JdbcMapper<T> mapper) {
    CachedRowSet cachedRowSet;
    ResultSetWrappingSqlRowSet rowSet;

    try (Connection connection = connectionPool.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query.query())) {
      query.parameters(preparedStatement);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.isBeforeFirst()) {
          cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
          cachedRowSet.populate(resultSet);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    // In order to use aliases, ResultSetWrappingSqlRowSet is necessary. This class comes from spring-jdbc dependency.
    rowSet = new ResultSetWrappingSqlRowSet(cachedRowSet);
    // here we move the cursor one step
    rowSet.next();
    return mapper.map(rowSet);
  }

  private long generateId(ResultSet generatedKeys) throws SQLException {
    if (generatedKeys.next()) {
      return generatedKeys.getLong(1);
    } else {
      throw new RuntimeException("DML failed, no ID obtained.");
    }
  }
}
