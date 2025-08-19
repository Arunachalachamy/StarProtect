<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>body{font-family:Arial,Helvetica,sans-serif;background:#f6f7fb}.card{max-width:520px;margin:40px auto;background:#fff;padding:24px;border-radius:8px;box-shadow:0 2px 10px rgba(0,0,0,.05)}input{width:100%;padding:10px;margin:8px 0;border:1px solid #ccd;border-radius:6px}button{width:100%;background:#0b5ed7;border:none;color:#fff;padding:10px;border-radius:6px;cursor:pointer}.error{color:#b00020;margin:8px 0;text-align:center}</style>
<div class="card">
  <h2>Customer Registration</h2>
  <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/register">
    <input name="loginId" placeholder="User Id" required />
    <input name="name" placeholder="Name" required />
    <input type="email" name="email" placeholder="Email" required />
    <input name="address" placeholder="Address" required />
    <input name="phone" placeholder="Phone" required />
    <input type="password" name="password" placeholder="Password" required />
    <button type="submit">Register</button>
  </form>
  <p>Already registered? <a href="${pageContext.request.contextPath}/login">Login</a></p>
</div>
