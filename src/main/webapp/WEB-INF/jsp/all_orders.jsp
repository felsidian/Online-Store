<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><fmt:message key="allOrders"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <nav aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><fmt:message key="allOrders"/></li>
        </ol>
    </nav>
    <div class="row">
        <div class="col">
            <h1 class="mb-3"><fmt:message key="allOrders"/></h1>
            <ul class="list-group" id="cart-list">
                <c:forEach items="${requestScope.orders}" var="order">
                    <!--ITEM-->
                    <c:set var="orderLink" value="${Path.ORDER_PATH += '?id=' += order.id}"/>
                    <li class="list-group-item">
                        <div class="row d-flex align-items-center">
                            <div class="col-6">
                                <a href="<c:out value="${orderLink}"/>"><fmt:message key="orderN"/><c:out value="${order.id}"/></a>
                            </div>
                            <div class="col-6">
                                 <h6 class="my-4"><fmt:message key="status"/>: <fmt:message key="${order.status}"/></h6>
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
