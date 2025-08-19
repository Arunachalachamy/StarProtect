package com.ebms.servlet;

import com.ebms.dao.BillDao;
import com.ebms.dao.CustomerDao;
import com.ebms.util.Db;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "AdminDeleteCustomerServlet", urlPatterns = "/admin/customers/delete")
public class AdminDeleteCustomerServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) { resp.sendError(403); return; }
		int id = Integer.parseInt(req.getParameter("id"));
		try (Connection conn = Db.getConnection(req)) {
			BillDao billDao = new BillDao();
			double due = billDao.pendingDueForCustomer(conn, id);
			if (due == 0) {
				billDao.deleteAllForCustomer(conn, id);
				new CustomerDao().delete(conn, id);
			}
			resp.sendRedirect(req.getContextPath() + "/admin/customers");
		} catch (Exception e) { throw new ServletException(e); }
	}
}

