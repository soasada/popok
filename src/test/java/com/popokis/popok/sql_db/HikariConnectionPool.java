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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HikariConnectionPool {

  private final DataSource dataSource;

  private HikariConnectionPool() {
    Properties properties = new Properties();
    final String configFilename = File.separator + "db_config.properties";

    try (InputStream fi = HikariConnectionPool.class.getResourceAsStream(configFilename)) {
      properties.load(fi);
    } catch (IOException e) {
      throw new RuntimeException(configFilename + " not found. Please create it inside resources folder.");
    }

    this.dataSource = new HikariDataSource(new HikariConfig(properties));
  }

  private static class Holder {
    private static final HikariConnectionPool INSTANCE = new HikariConnectionPool();
  }

  public static HikariConnectionPool getInstance() {
    return Holder.INSTANCE;
  }

  public DataSource get() {
    return dataSource;
  }
}
