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
<h1><fmt:message key="h1.goods.editing"/></h1>

<br>
<form action="edit" method="post">
    <label><b>${goods.getId()}</b></label>
    <input type="hidden" name="id" value="${goods.getId()}">
    <br>
    <input type="text" placeholder="<fmt:message key="placeholder.gname"/>" name="gname" value="${goods.getName()}" required>
    <br>
    <input type="text" placeholder="<fmt:message key="placeholder.gdescription"/>" name="gdescription" value="${goods.getDescription()}"
                   required>
    <br>
    <input type="number" step="0.01" min ="0" placeholder="<fmt:message key="placeholder.gprice"/>" name="gprice" value="${goods.getPrice()}"
                   required>
    <br>
    <input type="number" min ="0" placeholder="<fmt:message key="placeholder.gquantity"/>" name="gquantity" value="${goods.getQuantity()}"
                   required>
    <br>
    <button type="submit"><fmt:message key="button.save"/></button>
</form>
</body>
</html>