<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="_layout.jspf" %>
<div class="container">
  <h2>Welcome, ${sessionScope.userName}</h2>
  <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:16px;margin-top:12px">
    <div style="background:#f1f5ff;padding:16px;border-radius:8px"><div>Total Consumption</div><div style="font-size:24px;font-weight:bold">${totalConsumption}</div></div>
    <div style="background:#f1fff3;padding:16px;border-radius:8px"><div>Last Payment</div><div style="font-size:24px;font-weight:bold">${lastPayment}</div></div>
    <div style="background:#fff4f1;padding:16px;border-radius:8px"><div>Payment Due</div><div style="font-size:24px;font-weight:bold">${due}</div></div>
  </div>
</div>
