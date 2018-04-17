package com.popokis.popok.data;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

final class HikariConnectionPool {

  private final HikariDataSource dataSource;

  private HikariConnectionPool() {
    Config config = ConfigFactory.defaultApplication().resolve();
    HikariConfig hikariConfig;

    if (config.hasPath("test")) {
      hikariConfig = getTestConfig(config.getConfig("test"));
    } else {
      hikariConfig = getProdConfig(config.getConfig("prod"));
    }

    dataSource = new HikariDataSource(hikariConfig);
  }

  private static class Holder {
    private static final HikariConnectionPool INSTANCE = new HikariConnectionPool();
  }

  static HikariConnectionPool getInstance() {
    return Holder.INSTANCE;
  }

  Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  private HikariConfig getProdConfig(Config config) {
    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setMaximumPoolSize(config.getInt("maximumPoolSize"));
    hikariConfig.setMinimumIdle(config.getInt("minimumIdle"));
    hikariConfig.setJdbcUrl(config.getString("jdbcUrl"));
    hikariConfig.setUsername(config.getString("username"));
    hikariConfig.setPassword(config.getString("password"));
    hikariConfig.addDataSourceProperty("cachePrepStmts", config.getString("cachePrepStmts"));
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", config.getString("prepStmtCacheSize"));
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", config.getString("prepStmtCacheSqlLimit"));
    hikariConfig.addDataSourceProperty("useServerPrepStmts", config.getString("useServerPrepStmts"));

    return hikariConfig;
  }

  private HikariConfig getTestConfig(Config config) {
    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setDataSourceClassName(config.getString("dataSourceClassName"));
    hikariConfig.addDataSourceProperty("URL", config.getString("URL"));
    hikariConfig.addDataSourceProperty("user", config.getString("user"));
    hikariConfig.addDataSourceProperty("password", config.getString("password"));

    return hikariConfig;
  }
}
