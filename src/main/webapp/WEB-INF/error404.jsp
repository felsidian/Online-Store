<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><fmt:message key="leet"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex h-100 text-center">
    <div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
        <header class="mb-auto"></header>
        <main class="px-3">
            <h1><fmt:message key="oops"/></h1>
            <p class="lead"><fmt:message key="error404"/></p>
            <p class="lead">
                <a href="#" class="btn  btn-secondary fw-bold border-white" style=""><fmt:message key="backToHome"/></a>
            </p>
        </main>
        <footer class="mt-auto"></footer>
    </div>
</body>

</html>