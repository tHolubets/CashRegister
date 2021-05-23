<%@ page import="com.studing.cashRegister.model.Report" %>
<%@ page import="com.studing.cashRegister.model.User" %>
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
<%
    Report report = (Report)request.getAttribute("report");
    User user = (User)session.getAttribute("user");
%>
<h1><%=report.getName()%></h1>
<br>
<fmt:message key="report.check.quantity"/>: <%=report.getCheckQuantity()%>
<br>
<fmt:message key="report.total.amount"/>: <%=report.getTotalAmount()%> ₴
<br>
<fmt:message key="report.card.amount"/>: <%=report.getCardAmount()%> ₴
<br>
<fmt:message key="report.cash.amount"/>: <%=report.getCashAmount()%> ₴
<br>
<fmt:message key="report.tax.amount"/>: <%=report.getTaxAmount()%> ₴
<br><br>
<fmt:message key="report.r.check.quantity"/>: <%=report.getReturnedCheckQuantity()%>
<br>
<fmt:message key="report.r.total.amount"/>: <%=report.getReturnedTotalAmount()%> ₴
<br>
<fmt:message key="report.r.card.amount"/>: <%=report.getReturnedCardAmount()%> ₴
<br>
<fmt:message key="report.r.cash.amount"/>: <%=report.getReturnedCashAmount()%> ₴
<br>
<fmt:message key="report.r.tax.amount"/>: <%=report.getReturnedTaxAmount()%> ₴
<br><br>
<fmt:message key="report.cash.replenishment"/>: <%=report.getCashReplenishment()%> ₴
<br>
<fmt:message key="report.cash.withdrawal"/>: <%=report.getCashWithdrawal()%> ₴
<br><br>
<fmt:message key="text.time"/>: <%=report.getTime()%>
<br>
<fmt:message key="report.for.user"/>: <%=user.getFullName()%>
</body>
</html>