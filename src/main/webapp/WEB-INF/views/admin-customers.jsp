<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="_layout.jspf" %>
<div class="container">
  <h2>Customers</h2>
  <p><a href="${pageContext.request.contextPath}/admin/addbill">Add Bill</a></p>
  <table>
    <thead>
      <tr><th>ID</th><th>Login</th><th>Name</th><th>Email</th><th>Phone</th><th>Due</th><th>Actions</th></tr>
    </thead>
    <tbody>
      <c:forEach items="${customers}" var="c">
        <tr>
          <td>${c.customerId}</td>
          <td>${c.loginId}</td>
          <td>${c.name}</td>
          <td>${c.email}</td>
          <td>${c.phone}</td>
          <td>${dues[c.customerId]}</td>
          <td>
            <c:if test="${dues[c.customerId] == 0}">
              <form method="post" action="${pageContext.request.contextPath}/admin/customers/delete" onsubmit="return confirm('Delete customer '+ '${c.name}' + '?')" style="display:inline">
                <input type="hidden" name="id" value="${c.customerId}" />
                <button type="submit" style="background:#dc3545">Delete</button>
              </form>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
