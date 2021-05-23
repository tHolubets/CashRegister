<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title><fmt:message key="title.cash.register"/></title>
</head>
<body>
<h1><fmt:message key="h1.open.orders"/></h1>
<a href="${pageContext.request.contextPath}/logout"><fmt:message key="href.logout"/></a>
<br>
<%
    List<Order> orders = (List) session.getAttribute("openOrders");
    for (Order order : orders) {
        session.setAttribute("order", order);%>
<%@include file="orderPart.jsp" %>
<%}
    if(orders.size()==0){%>
<fmt:message key="text.no.open.orders"/>
<%}%>

<br><br><br>
<button onclick="location.href = window.location.href + '/xReport'"><fmt:message key="button.make.xreport"/></button>
<button onclick="location.href = window.location.href + '/zReport'"><fmt:message key="button.make.zreport"/></button>
</body>
</html>