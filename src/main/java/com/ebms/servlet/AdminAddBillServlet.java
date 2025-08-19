package com.ebms.servlet;

import com.ebms.dao.BillDao;
import com.ebms.dao.CustomerDao;
import com.ebms.model.Bill;
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

@WebServlet(name = "AdminAddBillServlet", urlPatterns = "/admin/addbill")
public class AdminAddBillServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
		try (Connection conn = Db.getConnection(req)) {
			CustomerDao dao = new CustomerDao();
			List<Customer> customers = dao.findAll(conn);
			req.setAttribute("customers", customers);
			req.getRequestDispatcher("/WEB-INF/views/admin-addbill.jsp").forward(req, resp);
		} catch (Exception e) { throw new ServletException(e); }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) { resp.sendError(403); return; }
		int customerId = Integer.parseInt(req.getParameter("customerId"));
		double meter = Double.parseDouble(req.getParameter("meter"));
		double amount = Double.parseDouble(req.getParameter("amount"));
		String month = req.getParameter("month");
		int year = Integer.parseInt(req.getParameter("year"));

		Bill b = new Bill();
		b.setCustomerId(customerId);
		b.setMeterReading(meter);
		b.setBillAmount(amount);
		b.setMonth(month);
		b.setYear(year);
		b.setStatus("PENDING");

		try (Connection conn = Db.getConnection(req)) {
			new BillDao().insert(conn, b);
			resp.sendRedirect(req.getContextPath() + "/admin/customers");
		} catch (Exception e) { throw new ServletException(e); }
	}
}

