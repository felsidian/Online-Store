package com.gmail.hryhoriev75.onlineshop.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton which helps to get DB connections using Hikari Connection Pooling
 * Use getInstance() method to get access
 */
public class DBHelper {

    public static final String HOST = "RDS_HOSTNAME";
    public static final String PORT = "RDS_PORT";
    public static final String DB_NAME = "RDS_DB_NAME";
    public static final String USERNAME = "RDS_USERNAME";
    public static final String PASSWORD = "RDS_PASSWORD";

    private static volatile DBHelper instance;

    private final HikariDataSource ds;

    private DBHelper() {
        String hostname = System.getProperty(HOST);
        String port = System.getProperty(PORT);
        String dbName = System.getProperty(DB_NAME);
        String userName = System.getProperty(USERNAME);
        String password = System.getProperty(PASSWORD);

        //configuring db connection and pooling with recommended params
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + dbName);
        config.setUsername(userName);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void shutdown() {
        ds.close();
    }

    public static DBHelper getInstance() {
        DBHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (DBHelper.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new DBHelper();
            }
        }
        return localInstance;
    }

}