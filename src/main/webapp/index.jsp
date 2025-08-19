<%@ page contentType="text/html;charset=UTF-8" %>
<%
  Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
  if (isAdmin != null && isAdmin) {
    response.sendRedirect(request.getContextPath() + "/admin/customers");
    return;
  }
  if (session.getAttribute("userId") != null) {
    response.sendRedirect(request.getContextPath() + "/dashboard");
    return;
  }
  response.sendRedirect(request.getContextPath() + "/login");
%>
