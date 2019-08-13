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
