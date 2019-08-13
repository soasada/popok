package com.popokis.popok.sql_db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "create")
public class Employee {
  @NonNull Long id;
  @NonNull String name;
  @NonNull Long departmentId;
}
