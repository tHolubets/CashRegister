<%@ page import="com.studing.cashRegister.model.Order" %>
<%@ page import="com.studing.cashRegister.model.OrderedGoods" %>
<%@ page import="java.util.List" %>
<%@ page import="com.studing.cashRegister.model.User" %>
<%@ page import="com.studing.cashRegister.model.Permission" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<html>

<%
    Order myOrder = (Order) session.getAttribute("order");
    User user = (User) session.getAttribute("user");
    boolean userCanEdit = user.getRole().hasPermission(Permission.CANCEL_ORDER) ? true : false;
    List<OrderedGoods> orderedGoodsList = myOrder.getGoodsList();
    request.setAttribute("items", orderedGoodsList);
%>

<head>
</head>
<body>
<h3><fmt:message key="h3.check"/> <%=myOrder.getId()%> (<%=myOrder.getStatus()%>)</h3>

<ul>
    <c:forEach var="goods" items="${items}">
        <li>${goods.forShow()}</li>
    </c:forEach>
</ul>

<%=myOrder.getDateTime()%>
<br><br>
<fmt:message key="text.total.amount"/> <b><%=myOrder.getTotalAmount()%>
</b>
<br><br>
<% if (session.getAttribute("cashierName") != null) {%>
<fmt:message key="text.cashier"/> ${cashierName}
<br><br>
<%}%>

<%if (userCanEdit) {%>
<button onclick="location.href = window.location.href + '/edit?id=' + <%=myOrder.getId()%>"><fmt:message key="button.edit.check"/></button>

<form action="openOrders/cancel" method="post">
    <input type="hidden" name="id" value="<%=myOrder.getId()%>">
    <button type="submit\"><fmt:message key="button.cancel.check"/></button>
</form>
<%}%>

</body>
</html>