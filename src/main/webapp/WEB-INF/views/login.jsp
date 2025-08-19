<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>body{font-family:Arial,Helvetica,sans-serif;background:#f6f7fb}.card{max-width:420px;margin:60px auto;background:#fff;padding:24px;border-radius:8px;box-shadow:0 2px 10px rgba(0,0,0,.05)}input{width:100%;padding:10px;margin:8px 0;border:1px solid #ccd;border-radius:6px}button{width:100%;background:#0b5ed7;border:none;color:#fff;padding:10px;border-radius:6px;cursor:pointer}.error{color:#b00020;margin:8px 0;text-align:center}.muted{color:#666;font-size:.9em;text-align:center}</style>
<div class="card">
  <h2>Login</h2>
  <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <input name="loginId" placeholder="User Id" required />
    <input type="password" name="password" placeholder="Password" required />
    <label style="display:flex;align-items:center;gap:8px;margin:8px 0"><input type="checkbox" name="admin"/> Login as admin</label>
    <button type="submit">Login</button>
  </form>
  <p class="muted">New user? <a href="${pageContext.request.contextPath}/register">Register</a></p>
</div>
