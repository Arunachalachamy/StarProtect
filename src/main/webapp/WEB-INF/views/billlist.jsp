<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="_layout.jspf" %>
<div class="container">
  <h2>Your Bills</h2>
  <form method="get" action="${pageContext.request.contextPath}/billlist" style="display:flex;gap:8px;margin:12px 0">
    <input name="q" value="${q}" placeholder="Search month/year/status"/>
    <button type="submit">Search</button>
  </form>
  <table>
    <thead>
      <tr><th>ID</th><th>Month</th><th>Year</th><th>Meter</th><th>Amount</th><th>Status</th><th>Action</th></tr>
    </thead>
    <tbody>
      <c:forEach items="${bills}" var="b">
        <tr>
          <td>${b.billId}</td>
          <td>${b.month}</td>
          <td>${b.year}</td>
          <td>${b.meterReading}</td>
          <td>${b.billAmount}</td>
          <td>${b.status}</td>
          <td>
            <c:choose>
              <c:when test="${b.status ne 'PAID'}">
                <form method="post" action="${pageContext.request.contextPath}/paybill" style="display:inline">
                  <input type="hidden" name="billId" value="${b.billId}" />
                  <input type="hidden" name="mode" value="COMPLETE" />
                  <button type="submit">Pay</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/paybill" style="display:inline">
                  <input type="hidden" name="billId" value="${b.billId}" />
                  <input type="hidden" name="mode" value="PARTIAL" />
                  <button type="submit" style="background:#6c757d">Partial</button>
                </form>
              </c:when>
              <c:otherwise>-</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
