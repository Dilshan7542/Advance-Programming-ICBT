package lk.icbt.dental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton connection provider. A new JDBC connection is returned for each
 * operation so callers can use try-with-resources safely.
 */
public final class DBConnection {
    private final String url;
    private final String username;
    private final String password;

    private DBConnection() {
        try {
            Class.forName(AppConfig.getOrDefault("db.driver", "com.mysql.cj.jdbc.Driver"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver is not available", e);
        }
        this.url = AppConfig.get("db.url");
        this.username = AppConfig.get("db.username");
        this.password = AppConfig.get("db.password");
    }

    private static final class Holder {
        private static final DBConnection INSTANCE = new DBConnection();
    }

    public static DBConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
