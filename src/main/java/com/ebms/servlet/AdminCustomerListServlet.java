package com.ebms.servlet;

import com.ebms.dao.CustomerDao;
import com.ebms.dao.BillDao;
import com.ebms.model.Customer;
import com.ebms.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name = "AdminCustomerListServlet", urlPatterns = "/admin/customers")
public class AdminCustomerListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
		try (Connection conn = Db.getConnection(req)) {
			CustomerDao dao = new CustomerDao();
			List<Customer> customers = dao.findAll(conn);
			Map<Integer, Double> dues = new HashMap<>();
			BillDao billDao = new BillDao();
			for (Customer c : customers) {
				dues.put(c.getCustomerId(), billDao.pendingDueForCustomer(conn, c.getCustomerId()));
			}
			req.setAttribute("customers", customers);
			req.setAttribute("dues", dues);
			req.getRequestDispatcher("/WEB-INF/views/admin-customers.jsp").forward(req, resp);
		} catch (Exception e) { throw new ServletException(e); }
	}
}

