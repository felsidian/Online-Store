<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><fmt:message key="allUsers"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <nav aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><fmt:message key="allUsers"/></li>
        </ol>
    </nav>
    <div class="row">
        <div class="col">
            <h1 class="mb-3"><fmt:message key="allUsers"/></h1>
            <ul class="list-group" id="cart-list">
                <c:forEach items="${requestScope.users}" var="user">
                    <!--ITEM-->
                    <li class="list-group-item">
                        <div class="row d-flex align-items-center">
                            <div class="col-6">
                                <p><c:out value="${user.name}"/><br><c:out value="${user.email}"/></p>
                            </div>
                            <div class="col-6">
                                <c:choose>
                                    <c:when test="${user.blocked}">
                                        <a class="btn btn-primary" href="${Path.UPDATE_PATH += '?userId=' += user.id += '&what=unblock'}" role="button"><fmt:message key="unblockUser"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-secondary" href="${Path.UPDATE_PATH += '?userId=' += user.id += '&what=block'}" role="button"><fmt:message key="blockUser"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </li>
                </c:forEach>
                <!--ITEM-->
            </ul>
        </div>
    </div>

</div>
<!-- /.container -->

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>

</html>
