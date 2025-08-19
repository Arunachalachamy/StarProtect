<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="_layout.jspf" %>
<div class="container">
  <h2>Add Bill</h2>
  <form method="post" action="${pageContext.request.contextPath}/admin/addbill" style="display:grid;grid-template-columns:1fr 1fr;gap:12px;max-width:720px">
    <label>Customer
      <select name="customerId" required>
        <c:forEach items="${customers}" var="c">
          <option value="${c.customerId}">${c.name} (${c.loginId})</option>
        </c:forEach>
      </select>
    </label>
    <label>Month
      <input name="month" placeholder="e.g. Jan" required />
    </label>
    <label>Year
      <input name="year" type="number" value="2025" required />
    </label>
    <label>Meter Reading
      <input name="meter" type="number" step="0.01" required />
    </label>
    <label>Amount
      <input name="amount" type="number" step="0.01" required />
    </label>
    <div style="grid-column:1/-1">
      <button type="submit">Add Bill</button>
    </div>
  </form>
</div>
