package com.popokis.popok.sql_db.model;

import com.popokis.popok.sql_db.JdbcMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "create")
public class Department implements JdbcMapper<Department> {
  @Builder.Default @NonNull Long id = 0L;
  @Builder.Default @NonNull LocalDateTime timestamp = LocalDateTime.now();
  @Builder.Default @NonNull String name = "";
  @Builder.Default @NonNull Set<Employee> employees = Set.of();

  @Override
  public Optional<Department> map(ResultSetWrappingSqlRowSet resultSet) {
    if (resultSet.getLong("id") == 0) return Optional.empty();

    return Optional.of(
        Department.create(
            resultSet.getLong("id"),
            resultSet.getTimestamp("timestamp").toLocalDateTime(),
            resultSet.getString("name"),
            Set.of()
        )
    );
  }
}
