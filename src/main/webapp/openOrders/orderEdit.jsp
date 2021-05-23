<%@ page import="com.studing.cashRegister.model.Order" %>
<%@ page import="com.studing.cashRegister.model.OrderedGoods" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<html>

<%
    Order myOrder = (Order) request.getAttribute("order");
    List<OrderedGoods> orderedGoodsList = myOrder.getGoodsList();
    request.setAttribute("items", orderedGoodsList);
    int i = 0;
%>

<head>
    <title><fmt:message key="title.cash.register"/></title>
</head>
<body>
<h3><fmt:message key="h3.check"/> <%=myOrder.getId()%> (<%=myOrder.getStatus()%>)</h3>

<form action="edit" method="post">
<ul>
    <c:forEach var="goods" items="${items}">
        <li>${goods.forShowEdit()}
            <input type="number" name="<%=i++%>"
                   min ="0" max = "${goods.getQuantity()}" value="${goods.getQuantity()}" required>
        </li>
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

    <input type="hidden" name="id" value="<%=myOrder.getId()%>">
    <button type="submit\"><fmt:message key="button.save.check"/></button>
</form>

</body>
</html>