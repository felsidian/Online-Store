<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.gmail.hryhoriev75.onlineshop.model.entity.User.Role" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en"/>
<fmt:setBundle basename="messages"/>

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="title.catalog" /></title>
    <link rel="stylesheet" href="style/catalog.css">
</head>
<body>
    <div style="float:right;">
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <p>Hello, <c:out value="${sessionScope.user.email}"/> <button type="button" onclick="window.location.href='/logout';">Logout</button></p>
            </c:when>
            <c:otherwise>
                <button type="button" onclick="window.location.href='/login';">Log in</button>
                <button type="button" onclick="window.location.href='/signup';">Sign up</button>
            </c:otherwise>
        </c:choose>
    </div>
    <h1 style="padding: 24px 30px">Product catalog</h1>
        <c:choose>
            <c:when test="${sessionScope.user.role.admin}"> <%-- sessionScope.user.role == Role.ADMIN doesnt work. maybe refactor with custom tag which can compare Enums)--%>
                <p style="padding-left:24px;">You are ADMIN. There will be admin specific buttons and links that allow u to edit products, view orders etc.</p>
            </c:when>
            <c:when test="${sessionScope.user.role.user}">
                <p style="padding-left:24px;">You are registered user. There will be functionality to add products to cart and make orders.</p>
            </c:when>
            <c:otherwise>
                <p style="padding-left:24px;">You are not logged in. You just can view products</p>
            </c:otherwise>
        </c:choose>
    <div style="padding: 60px 30px;">
        <div class="row">
            <div class="column" style="background-color:#aaa;">
                <h2>Product 1</h2>
                <p>Description..</p>
            </div>
            <div class="column" style="background-color:#bbb;">
                <h2>Product 2</h2>
                <p>Description..</p>
            </div>
        </div>

        <div class="row">
            <div class="column" style="background-color:#ccc;">
                <h2>Product 3</h2>
                <p>Description..</p>
            </div>
            <div class="column" style="background-color:#ddd;">
                <h2>Product 4</h2>
                <p>Description..</p>
            </div>
        </div>

        <div class="row">
            <div class="column" style="background-color:#ccc;">
                <h2>Product 5</h2>
                <p>Description..</p>
            </div>
            <div class="column" style="background-color:#ddd;">
                <h2>Product 6</h2>
                <p>Description..</p>
            </div>
        </div>

        <div class="row">
            <div class="column" style="background-color:#ccc;">
                <h2>Product 7</h2>
                <p>Description..</p>
            </div>
            <div class="column" style="background-color:#ddd;">
                <h2>Product 8</h2>
                <p>Description..</p>
            </div>
        </div>

        <div class="row">
            <div class="column" style="background-color:#ccc;">
                <h2>Product 9</h2>
                <p>Description..</p>
            </div>
            <div class="column" style="background-color:#ddd;">
                <h2>Product 10</h2>
                <p>Description..</p>
            </div>
        </div>
    </div>
</body>
</html>
