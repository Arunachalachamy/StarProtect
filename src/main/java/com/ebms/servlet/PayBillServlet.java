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

@WebServlet(name = "PayBillServlet", urlPatterns = "/paybill")
public class PayBillServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object userIdObj = req.getSession().getAttribute("userId");
		if (userIdObj == null || Boolean.TRUE.equals(req.getSession().getAttribute("isAdmin"))) {
			resp.sendError(403);
			return;
		}

		int billId = Integer.parseInt(req.getParameter("billId"));
		String mode = req.getParameter("mode"); // COMPLETE or PARTIAL

		try (Connection conn = Db.getConnection(req)) {
			BillDao dao = new BillDao();
			Bill b = dao.findById(conn, billId);
			if (b == null) { resp.sendError(404); return; }
			String status = "COMPLETE".equalsIgnoreCase(mode) ? "PAID" : "PARTIAL";
			dao.updateStatus(conn, billId, status);
			resp.sendRedirect(req.getContextPath() + "/billlist");
		} catch (Exception e) { throw new ServletException(e); }
	}
}

