package com.ebms.servlet;

import com.ebms.dao.BillDao;
import com.ebms.model.Bill;
import com.ebms.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object userIdObj = req.getSession().getAttribute("userId");
		if (userIdObj == null || Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		int userId = (int) userIdObj;
		try (Connection conn = Db.getConnection(req)) {
			BillDao billDao = new BillDao();
			List<Bill> bills = billDao.listByCustomer(conn, userId, null);
			double totalConsumption = bills.stream().mapToDouble(Bill::getMeterReading).sum();
			double lastPayment = bills.stream().filter(b -> "PAID".equalsIgnoreCase(b.getStatus()))
					.mapToDouble(Bill::getBillAmount).findFirst().orElse(0);
			double due = bills.stream().filter(b -> !"PAID".equalsIgnoreCase(b.getStatus()))
					.mapToDouble(Bill::getBillAmount).sum();

			req.setAttribute("totalConsumption", totalConsumption);
			req.setAttribute("lastPayment", lastPayment);
			req.setAttribute("due", due);
			req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
		} catch (Exception e) { throw new ServletException(e); }
	}
}

