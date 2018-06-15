package com.popokis.popok.data.access;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class HikariConnectionPool implements ConnectionPool<HikariDataSource> {

  private final HikariDataSource dataSource;

  private HikariConnectionPool() {
    Config config = ConfigFactory.defaultApplication().resolve();
    HikariConfig hikariConfig = null;

    switch (config.getString("appEnv")) {
      case "test":
        hikariConfig = getConfig(config.getConfig("test"));
        break;
      case "prod":
        hikariConfig = getConfig(config.getConfig("prod"));
        break;
      case "debug":
        hikariConfig = getConfig(config.getConfig("debug"));
        break;
    }

    dataSource = new HikariDataSource(hikariConfig);
  }

  private static class Holder {
    private static final ConnectionPool<HikariDataSource> INSTANCE = new HikariConnectionPool();
  }

  public static ConnectionPool<HikariDataSource> getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public HikariDataSource pool() {
    return dataSource;
  }

  @Override
  public Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private HikariConfig getConfig(Config config) {
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
}
