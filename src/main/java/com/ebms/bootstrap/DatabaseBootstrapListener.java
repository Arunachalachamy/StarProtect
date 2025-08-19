package com.ebms.bootstrap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DatabaseBootstrapListener implements ServletContextListener {

	public static final String JDBC_URL_ATTR = "EBMS_JDBC_URL";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		String dbPathConfig = context.getInitParameter("DERBY_DB_PATH");
		String dbDir = (dbPathConfig == null || dbPathConfig.isEmpty()) ? System.getProperty("java.io.tmpdir") + "/ebms-db" : dbPathConfig;

		try {
			Path db = Paths.get(dbDir);
			Files.createDirectories(db);
			String jdbcUrl = "jdbc:derby:" + db.toAbsolutePath().toString() + ";create=true";
			context.setAttribute(JDBC_URL_ATTR, jdbcUrl);

			initializeSchema(jdbcUrl);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Derby database", e);
		}
	}

	private void initializeSchema(String jdbcUrl) throws SQLException {
		try (Connection conn = DriverManager.getConnection(jdbcUrl)) {
			try (Statement st = conn.createStatement()) {
				st.executeUpdate("create table customers (" +
						"customer_id int generated always as identity primary key, " +
						"login_id varchar(64) not null unique, " +
						"name varchar(128) not null, " +
						"email varchar(128) not null, " +
						"address varchar(256) not null, " +
						"phone varchar(32) not null, " +
						"password_hash varchar(256) not null)");
			} catch (SQLException ignored) { }

			try (Statement st = conn.createStatement()) {
				st.executeUpdate("create table bills (" +
						"bill_id int generated always as identity primary key, " +
						"meter_reading double not null, " +
						"bill_amount double not null, " +
						"customer_id int not null, " +
						"month varchar(16) not null, " +
						"year int not null, " +
						"status varchar(16) not null, " +
						"constraint fk_bill_customer foreign key (customer_id) references customers(customer_id))");
			} catch (SQLException ignored) { }
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Derby embedded shuts down when JVM stops; nothing required here.
	}
}

