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

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String loginId = req.getParameter("loginId");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String address = req.getParameter("address");
		String phone = req.getParameter("phone");
		String password = req.getParameter("password");

		if (isAnyEmpty(loginId, name, email, address, phone, password)) {
			req.setAttribute("error", "All fields are mandatory");
			req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
			return;
		}

		Customer c = new Customer();
		c.setLoginId(loginId);
		c.setName(name);
		c.setEmail(email);
		c.setAddress(address);
		c.setPhone(phone);
		c.setPasswordHash(PasswordUtil.sha256(password));

		try (Connection conn = Db.getConnection(req)) {
			CustomerDao dao = new CustomerDao();
			if (dao.findByLoginId(conn, loginId) != null) {
				req.setAttribute("error", "Login Id already exists");
				req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
				return;
			}
			dao.insert(conn, c);
			req.getSession().setAttribute("userId", c.getCustomerId());
			req.getSession().setAttribute("userName", c.getName());
			req.getSession().setAttribute("isAdmin", Boolean.FALSE);
			resp.sendRedirect(req.getContextPath() + "/dashboard");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private boolean isAnyEmpty(String... v) { for (String s : v) if (s == null || s.trim().isEmpty()) return true; return false; }
}

