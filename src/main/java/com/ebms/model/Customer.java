package com.ebms.model;

public class Customer {
	private Integer customerId;
	private String loginId;
	private String name;
	private String email;
	private String address;
	private String phone;
	private String passwordHash;

	public Integer getCustomerId() { return customerId; }
	public void setCustomerId(Integer customerId) { this.customerId = customerId; }

	public String getLoginId() { return loginId; }
	public void setLoginId(String loginId) { this.loginId = loginId; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getPasswordHash() { return passwordHash; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}

