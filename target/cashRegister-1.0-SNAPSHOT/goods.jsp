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
<h1><fmt:message key="h1.goods.adding"/></h1>
<a href="${pageContext.request.contextPath}/logout"><fmt:message key="href.logout"/></a>
<br>
<%
    List<Goods> goodsList = (List) request.getAttribute("goods");
    for (Goods goods : goodsList) {
        session.setAttribute("goodsPart", goods);%>
<%@include file="goodsPart.jsp" %>
<%
    }
    if (goodsList.size() == 0) {
%>
<fmt:message key="text.no.goods"/>
<%}%>
<br><br>
<form action="goods/edit" method="post">
    <input type="text" placeholder="<fmt:message key="placeholder.gname"/>" name="gname" required>
    <br>
    <input type="text" placeholder="<fmt:message key="placeholder.gdescription"/>" name="gdescription" required>
    <br>
    <input type="number" step="0.01" min="0" placeholder="<fmt:message key="placeholder.gprice"/>" name="gprice" required>
    <br>
    <input type="number" min="0" placeholder="<fmt:message key="placeholder.gquantity"/>" name="gquantity" required>
    <br>
    <button type="submit"><fmt:message key="button.add.goods"/></button>
</form>

<script type="text/javascript">
    function editRedirect(id) {
        location.href = window.location.href + "/edit?id=" + id;
    };
</script>
</body>
</html>