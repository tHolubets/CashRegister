<%@ page import="com.studing.cashRegister.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.studing.cashRegister.model.Goods" %>
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
<%
    Goods goodsPart = (Goods) session.getAttribute("goodsPart");
%>

<table onclick="editRedirect(<%=goodsPart.getId()%>)">
<tr>
    <td><%=goodsPart.getId()%>.</td>
    <td><b><%=goodsPart.getName()%></b></td>
    <td><i><%=goodsPart.getDescription()%></i></td>
    <td>{<%=goodsPart.getPrice()%> ₴}</td>
    <td>[<%=goodsPart.getQuantity()%>]</td>
</tr>
</table>
</body>
</html>