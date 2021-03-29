<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><fmt:message key="orderN"/><c:out value="${requestScope.order.id}"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <nav aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
            <li class="breadcrumb-item"><a href="<c:out value="${Path.ORDERS_PATH}"/>"><fmt:message key="myOrders"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><fmt:message key="orderN"/><c:out value="${requestScope.order.id}"/></li>
        </ol>
    </nav>
    <div class="row">
        <div class="col">
            <h4 class="my-4"><fmt:message key="orderN"/><c:out value="${requestScope.order.id}"/></h4>
            <h6 class="my-4"><fmt:message key="createDate"/>: <fmt:formatDate type="both" value="${requestScope.order.createTime}"/></h6>
            <h6 class="my-4"><fmt:message key="status"/>: <fmt:message key="${requestScope.order.status}"/></h6>
            <ul class="list-group" id="cart-list">
                <c:forEach items="${requestScope.orderContent}" var="record">
                    <!--ITEM-->
                    <c:set var="productLink" value="${Path.PRODUCT_PATH += '?id=' += record.product.id}"/>
                    <li class="list-group-item">
                        <div class="row d-flex align-items-center">
                            <div class="col-3">
                                <a href="<c:out value="${productLink}"/>"><img class="card-img-top p-3" src="<c:out value="${record.product.imageUrl}"/>" style="height: 100px; object-fit: scale-down;" alt=""></a>
                            </div>
                            <div class="col-9">
                                <div class="row d-flex align-items-center">
                                    <div class="col-7">
                                        <div class="product-name">
                                            <a href="<c:out value="${productLink}"/>"><c:out value="${record.product.name}"/></a>
                                        </div>
                                    </div>
                                    <div class="col-2">
                                        <input type="number" value="<c:out value="${record.quantity}"/>" class="form-control quantity-input" disabled>
                                    </div>
                                    <div class="col-3">
                                        <span><c:out value="${record.price}"/> грн</span>
                                    </div>
                                </div>
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
