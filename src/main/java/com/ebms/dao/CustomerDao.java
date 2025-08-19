package com.ebms.dao;

import com.ebms.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

	public void insert(Connection conn, Customer c) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(
				"insert into customers (login_id, name, email, address, phone, password_hash) values (?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, c.getLoginId());
			ps.setString(2, c.getName());
			ps.setString(3, c.getEmail());
			ps.setString(4, c.getAddress());
			ps.setString(5, c.getPhone());
			ps.setString(6, c.getPasswordHash());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					c.setCustomerId(rs.getInt(1));
				}
			}
		}
	}

	public Customer findByLoginId(Connection conn, String loginId) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("select * from customers where login_id=?")) {
			ps.setString(1, loginId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return map(rs);
			}
		}
		return null;
	}

	public Customer findById(Connection conn, int id) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("select * from customers where customer_id=?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) return map(rs);
			}
		}
		return null;
	}

	public List<Customer> findAll(Connection conn) throws SQLException {
		List<Customer> list = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement("select * from customers order by customer_id desc")) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(map(rs));
			}
		}
		return list;
	}

	public void delete(Connection conn, int id) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("delete from customers where customer_id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	private Customer map(ResultSet rs) throws SQLException {
		Customer c = new Customer();
		c.setCustomerId(rs.getInt("customer_id"));
		c.setLoginId(rs.getString("login_id"));
		c.setName(rs.getString("name"));
		c.setEmail(rs.getString("email"));
		c.setAddress(rs.getString("address"));
		c.setPhone(rs.getString("phone"));
		c.setPasswordHash(rs.getString("password_hash"));
		return c;
	}
}

