package com.ebms.servlet;

import com.ebms.dao.CustomerDao;
import com.ebms.model.Customer;
import com.ebms.security.PasswordUtil;
import com.ebms.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginId = req.getParameter("loginId");
		String password = req.getParameter("password");
		String isAdminParam = req.getParameter("admin");
		boolean asAdmin = "on".equalsIgnoreCase(isAdminParam) || "true".equalsIgnoreCase(isAdminParam);

		try (Connection conn = Db.getConnection(req)) {
			if (asAdmin) {
				// Simple built-in admin account for demo
				if ("admin".equals(loginId) && "admin123".equals(password)) {
					req.getSession().setAttribute("userId", -1);
					req.getSession().setAttribute("userName", "Admin");
					req.getSession().setAttribute("isAdmin", Boolean.TRUE);
					resp.sendRedirect(req.getContextPath() + "/admin/customers");
					return;
				}
				req.setAttribute("error", "Invalid admin credentials");
				req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
				return;
			}

			CustomerDao dao = new CustomerDao();
			Customer c = dao.findByLoginId(conn, loginId);
			if (c != null && c.getPasswordHash().equals(PasswordUtil.sha256(password))) {
				req.getSession().setAttribute("userId", c.getCustomerId());
				req.getSession().setAttribute("userName", c.getName());
				req.getSession().setAttribute("isAdmin", Boolean.FALSE);
				resp.sendRedirect(req.getContextPath() + "/dashboard");
				return;
			}
			req.setAttribute("error", "Invalid credentials");
			req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}

