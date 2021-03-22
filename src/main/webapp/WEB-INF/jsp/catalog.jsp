<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String email = null;
    if(session != null) {
        email = (String)session.getAttribute("email");
    }
%>

<fmt:setLocale value="ua"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="title.catalog" /></title>
</head>
<body>
    <h1><fmt:message key="title.catalog" /></h1>
    <br/>
    <% if (email != null) {%>
    <h2>Hello, <%=email%></h2>
    <% } %>

</body>
</html>