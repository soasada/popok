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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ListMapper<T> implements JdbcMapper<List<T>> {

  private final JdbcMapper<T> modelMapper;

  private ListMapper(JdbcMapper<T> modelMapper) {
    this.modelMapper = modelMapper;
  }

  public static <T> ListMapper<T> of(JdbcMapper<T> modelMapper) {
    return new ListMapper<>(modelMapper);
  }

  @Override
  public Optional<List<T>> map(ResultSetWrappingSqlRowSet rowSet) {
    List<T> resultList = new ArrayList<>();

    do {
      Optional<T> optional = modelMapper.map(rowSet);
      optional.ifPresent(resultList::add);
    } while (rowSet.next());

    return Optional.of(resultList);
  }
}
