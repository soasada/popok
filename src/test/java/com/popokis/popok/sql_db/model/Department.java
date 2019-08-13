package com.popokis.popok.sql_db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "create")
public class Department {
  @NonNull Long id;
  @NonNull LocalDateTime timestamp;
  @NonNull String name;
  @Nullable List<Employee> employees;
}
