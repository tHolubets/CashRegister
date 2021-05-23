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
${errorMessage}
</body>
</html>