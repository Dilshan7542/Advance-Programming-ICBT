package lk.icbt.dental.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lk.icbt.dental.util.AppConfig;
import lk.icbt.dental.util.DBConnection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class DbMigrationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (!AppConfig.getBoolean("db.auto-init", true)) {
            event.getServletContext().log("Database auto-initialization is disabled.");
            return;
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (input == null) {
                throw new IllegalStateException("db/schema.sql was not found");
            }
            String sql = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                    .lines()
                    .filter(line -> !line.trim().startsWith("--"))
                    .collect(Collectors.joining("\n"));

            try (Connection connection = DBConnection.getInstance().getConnection();
                 Statement statement = connection.createStatement()) {
                for (String command : sql.split(";")) {
                    String trimmed = command.trim();
                    if (!trimmed.isEmpty()) {
                        statement.execute(trimmed);
                    }
                }
            }
            event.getServletContext().log("Sunrise Dental Clinic database initialized successfully.");
        } catch (Exception e) {
            event.getServletContext().log("Database initialization failed. Check application.properties.", e);
        }
    }
}
