package com.ebms.dao;

import com.ebms.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

	public void insert(Connection conn, Bill b) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(
				"insert into bills (meter_reading, bill_amount, customer_id, month, year, status) values (?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setDouble(1, b.getMeterReading());
			ps.setDouble(2, b.getBillAmount());
			ps.setInt(3, b.getCustomerId());
			ps.setString(4, b.getMonth());
			ps.setInt(5, b.getYear());
			ps.setString(6, b.getStatus());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) b.setBillId(rs.getInt(1));
			}
		}
	}

	public List<Bill> listByCustomer(Connection conn, int customerId, String search) throws SQLException {
		String sql = "select * from bills where customer_id=?" + (search != null && !search.isEmpty() ? " and (lower(month) like ? or cast(year as varchar(10)) like ? or lower(status) like ?)" : "") + " order by year desc, bill_id desc";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			if (search != null && !search.isEmpty()) {
				String like = "%" + search.toLowerCase() + "%";
				ps.setString(2, like);
				ps.setString(3, like);
				ps.setString(4, like);
			}
			List<Bill> list = new ArrayList<>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(map(rs));
			}
			return list;
		}
	}

	public Bill findById(Connection conn, int billId) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("select * from bills where bill_id=?")) {
			ps.setInt(1, billId);
			try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
		}
		return null;
	}

	public void updateStatus(Connection conn, int billId, String status) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("update bills set status=? where bill_id=?")) {
			ps.setString(1, status);
			ps.setInt(2, billId);
			ps.executeUpdate();
		}
	}

	public List<Bill> listAll(Connection conn) throws SQLException {
		List<Bill> list = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("select * from bills order by year desc, bill_id desc")) {
			try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(map(rs)); }
		}
		return list;
	}

	public double pendingDueForCustomer(Connection conn, int customerId) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(
				"select coalesce(sum(bill_amount),0) as due from bills where customer_id=? and upper(status) <> 'PAID'")) {
			ps.setInt(1, customerId);
			try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getDouble("due"); }
		}
		return 0;
	}

	public void deleteAllForCustomer(Connection conn, int customerId) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("delete from bills where customer_id=?")) {
			ps.setInt(1, customerId);
			ps.executeUpdate();
		}
	}

	private Bill map(ResultSet rs) throws SQLException {
		Bill b = new Bill();
		b.setBillId(rs.getInt("bill_id"));
		b.setMeterReading(rs.getDouble("meter_reading"));
		b.setBillAmount(rs.getDouble("bill_amount"));
		b.setCustomerId(rs.getInt("customer_id"));
		b.setMonth(rs.getString("month"));
		b.setYear(rs.getInt("year"));
		b.setStatus(rs.getString("status"));
		return b;
	}
}

