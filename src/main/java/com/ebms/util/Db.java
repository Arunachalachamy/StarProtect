package com.ebms.util;

import com.ebms.bootstrap.DatabaseBootstrapListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

	public static Connection getConnection(HttpServletRequest req) throws SQLException {
		ServletContext ctx = req.getServletContext();
		String url = (String) ctx.getAttribute(DatabaseBootstrapListener.JDBC_URL_ATTR);
		return DriverManager.getConnection(url);
	}
}

