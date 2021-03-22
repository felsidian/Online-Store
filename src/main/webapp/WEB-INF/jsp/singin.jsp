<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="ua"/>
<fmt:setBundle basename="messages"/>
<%--<fmt:message key="title.singin" />--%>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/login.css">
</head>
<body>

<h2>Login Form
    <div><c:out value="${error}" /></div>
</h2>

<form action="/singin" method="post">

    <div class="container">
        <label for="email"><b>Email</b></label>
        <input type="text" placeholder="Enter Username" name="email" required value="<c:out value="${email}" default="" />">

        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <button type="submit">Login</button>
    </div>

    <div class="container" style="background-color:#f1f1f1">
        <button type="button" class="cancelbtn" onclick="window.location.href='/';">Cancel</button>
    </div>
</form>

</body>
</html>