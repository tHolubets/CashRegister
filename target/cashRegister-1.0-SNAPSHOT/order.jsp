<%@ page import="com.studing.cashRegister.model.Order" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html>
<head>
    <title><fmt:message key="title.cash.register"/></title>
</head>
<body>
<h1><fmt:message key="h1.order.creating"/></h1>

<a href="${pageContext.request.contextPath}/logout"><fmt:message key="href.logout"/></a>

<% Order order = (Order)session.getAttribute("order");%>

<% if (order != null) { %>
<%@include file="orderPart.jsp" %>

<% if (order.getStatus().equals("open")) { %>
<form action="order/addGoods" method="post">
    <input type="text" placeholder="<fmt:message key="placeholder.gname"/>" name="gname">
    <br>
    <input type="number" min ="1" placeholder="<fmt:message key="placeholder.gid"/>" name="gid">
    <br>
    <input type="number" min ="1" placeholder="<fmt:message key="placeholder.gquantity"/>" name="gquantity" value="1" required>
    <br>
    <span style="color:red">
                <%=(session.getAttribute("addPositionError") == null) ? "" : session.getAttribute("addPositionError")%>
        </span>
    <br>
    <button type="submit"><fmt:message key="button.add.goods"/></button>
</form>
<br>
<form action="order/close" method="post">
    <button type=\"submit\"><fmt:message key="button.close.order"/></button>
</form>
<% }} %>

<% if (order == null || !order.getStatus().equals("open")) { %>
<form method="post">
    <button type="submit"><fmt:message key="button.create.order"/></button>
</form>
<% } %>

</body>
</html>