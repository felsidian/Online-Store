<%@ page import="com.gmail.hryhoriev75.onlineshop.model.entity.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String email = null;
    String rolename = null;
    User user = null;
    if(session != null) {
        email = (String)session.getAttribute("email");
        rolename = (String)session.getAttribute("rolename");
        user = (User)session.getAttribute("user");
    }
%>

<fmt:setLocale value="ua"/>
<fmt:setBundle basename="messages"/>


<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="title.catalog" /></title>
    <link rel="stylesheet" href="style/catalog.css">
</head>
<body>
<div style="float:right;">
    <% if (email != null) {%>
    <p>Hello, <c:out value="${email}"/> <button type="button" onclick="window.location.href='/logout';">Logout</button></p>
    <% } else {%>
    <button type="button" onclick="window.location.href='/login';">Log in</button>
    <button type="button">Sing up</button>
    <% }  %>
</div>
<h1 style="padding: 24px 30px">Product catalog</h1>
<% if ("admin".equals(rolename)) {%>
    <p style="padding-left:24px;">You are ADMIN. There will be admin specific buttons and links that allow u to edit products, view orders etc.</p>
<% } else if ("user".equals(rolename)) {%>
    <p style="padding-left:24px;">You are registered user. There will be functionality to add products to cart and make orders.</p>
<% } else { %>
    <p style="padding-left:24px;">You are not logged in. You just can view products</p>
<% } %>

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
