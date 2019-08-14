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

import java.util.Optional;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "create")
public class Employee implements JdbcMapper<Employee> {
  @Builder.Default @NonNull Long id = 0L;
  @Builder.Default @NonNull String name = "";
  @Builder.Default @NonNull Long departmentId = 0L;

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
