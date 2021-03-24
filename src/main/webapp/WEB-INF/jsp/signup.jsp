<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en"/>
<fmt:setBundle basename="messages"/>
<%--<fmt:message key="title.singin" />--%>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/login.css">
    <title><fmt:message key="title.signup"/></title>
</head>

<body>
<div class="outer">
    <h2><fmt:message key="title.signup"/></h2>

    <form action="signup" method="post">
        <div class="container">
            <label for="email"><b>Email</b></label>
            <input type="text" placeholder="Enter Username" id="email" name="email" required value="<c:out value="${email}" default="" />">

            <label for="password"><b>Password</b></label>
            <input type="password" placeholder="Enter Password" id="password" name="password" required >

            <label for="name"><b>Name</b></label>
            <input type="text" placeholder="Enter name" id="name" name="name" required value="<c:out value="${name}" default="" />">

            <label for="phoneNumber"><b>Phone number</b></label>
            <input type="text" placeholder="Enter phone number" id="phoneNumber" name="phoneNumber" value="<c:out value="${phoneNumber}" default="" />">

            <div style="color:red; text-align: center;"><c:out value="${error}" /></div>

            <button type="submit">Sign up</button>
        </div>

        <div class="container" style="background-color:#f1f1f1">
            <button type="button" class="cancelbtn" onclick="window.location.href='/';">Cancel</button>
        </div>
    </form>
</div>
</body>
</html>