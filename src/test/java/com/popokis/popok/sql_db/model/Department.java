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
