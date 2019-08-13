package com.popokis.popok.sql_db.model;

import com.popokis.popok.sql_db.JdbcMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.util.Optional;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "create")
public class Employee implements JdbcMapper<Employee> {
  @NonNull Long id;
  @NonNull String name;
  @NonNull Long departmentId;

  @Override
  public Optional<Employee> map(ResultSetWrappingSqlRowSet resultSet) {
    if (resultSet.getLong("id") == 0) return Optional.empty();

    return Optional.of(
        Employee.create(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getLong("department_id")
        )
    );
  }
}
