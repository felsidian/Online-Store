<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="">
    <link rel="stylesheet" href="css/login.css">
    <title><fmt:message key="login"/></title>
</head>

<body>
    <div class="outer">
    <h2><fmt:message key="login"/></h2>

        <form action="${Path.LOGIN_PATH}" method="post">
            <div class="container">
                <label for="email"><b><fmt:message key="email"/></b></label>
                <input type="email" placeholder="<fmt:message key="enterEmail"/>" id="email" name="email" required value="<c:out value="${email}" default="" />">

                <label for="password"><b><fmt:message key="password"/></b></label>
                <input type="password" placeholder="<fmt:message key="enterPassword"/>" id="password" name="password" minlength="8" maxlength="64" required>

                <div style="color:red; text-align: center;"><c:out value="${error}" /></div>

                <button type="submit"><fmt:message key="login"/></button>
                <label>
                    <input type="checkbox" name="remember" <c:out value="${remember}" />> <fmt:message key="remember"/>
                </label>
            </div>

            <div class="container" style="background-color:#f1f1f1">
                <button type="button" class="cancelbtn" onclick="window.location.href='/';"><fmt:message key="cancel"/></button>
            </div>
        </form>
    </div>
</body>
</html>