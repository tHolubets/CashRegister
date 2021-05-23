<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="myt" uri="myTags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="dt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title><fmt:message key="title.cash.register"/></title>
</head>
<body>
<h1>
    <fmt:message key="h1.cash.register"/>
</h1>
<ul>
    <li><a href="?lang=en"><fmt:message key="label.lang.en"/></a></li>
    <li><a href="?lang=ua"><fmt:message key="label.lang.ua"/></a></li>
</ul>

<form action="login" method="post">
    <div class="imgcontainer">
        <img src="images/cashRegister.png" alt="Avatar" class="avatar">
    </div>

    <div class="container">
        <label><b><fmt:message key="label.username"/></b></label>
        <input type="text" placeholder="<fmt:message key="placeholder.username"/>" name="uname" required>
        <br>
        <label><b><fmt:message key="label.password"/></b></label>
        <input type="password" placeholder="<fmt:message key="placeholder.password"/>" name="upassword" required>
        <br>
        <span style="color:red">
                <%=(request.getAttribute("errMessage") == null) ? "" : request.getAttribute("errMessage")%>
        </span>
        <br><br>
        <button type="submit"><fmt:message key="button.login"/></button>
    </div>
</form>

<br><br><br>
<dt:dateTag/>
<br>
<myt:timeTag/>

</body>
</html>