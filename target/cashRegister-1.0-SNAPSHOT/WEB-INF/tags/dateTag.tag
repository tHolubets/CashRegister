<%@ tag import="java.time.LocalDate" %>
<%@ tag import="java.time.format.DateTimeFormatterBuilder" %>

<%
    LocalDate date = LocalDate.now();
    out.println("Date: " + date.format(new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy").toFormatter()));
%>