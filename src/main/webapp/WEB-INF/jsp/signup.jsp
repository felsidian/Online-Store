<%@include file="/WEB-INF/jspf/header.jspf" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="">
    <link rel="stylesheet" href="css/login.css">
    <title><fmt:message key="signup"/></title>
</head>

<body>
<div class="outer">
    <h2><fmt:message key="signup"/></h2>

    <form action="${Path.SIGNUP_PATH}" method="post">
        <div class="container">
            <label for="email"><b><fmt:message key="email"/></b></label>
            <input type="email" class="inp" placeholder="<fmt:message key="email"/>" id="email" name="email" required value="<c:out value="${requestScope.email}" default="" />">

            <label for="password"><b><fmt:message key="password"/></b></label>
            <input type="password" class="inp" placeholder="<fmt:message key="enterPassword"/>" id="password" name="password" minlength="8" maxlength="64" required >

            <label for="name"><b><fmt:message key="name"/></b></label>
            <input type="text" class="inp" placeholder="<fmt:message key="enterName"/>" id="name" name="name" required value="<c:out value="${requestScope.name}" default="" />">

<%--            <label for="phoneNumber"><b><fmt:message key="phoneNumber"/></b></label>--%>
<%--            <input type="tel" placeholder="<fmt:message key="enterPhoneNumber"/>" id="phoneNumber" name="phoneNumber" value="<c:out value="${phoneNumber}" default="" />">--%>

            <c:choose>
                <c:when test="${requestScope.error != null}">
                    <div style="color:red; text-align: center;"><fmt:message key="${requestScope.error}"/></div>
                </c:when>
            </c:choose>

            <button type="submit"><fmt:message key="signup"/></button>
        </div>

        <div class="container" style="background-color:#f1f1f1">
            <button type="button" class="cancelbtn" onclick="window.location.href='/';"><fmt:message key="cancel"/></button>
        </div>
    </form>
</div>
</body>
</html>