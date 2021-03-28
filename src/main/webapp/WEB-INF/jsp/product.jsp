<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><c:out value="${requestScope.product.name}"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <nav aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
            <li class="breadcrumb-item"><a href="<c:out value="${Path.CATEGORY_PATH += '?id=' += requestScope.product.category.id}"/>"><c:out value="${requestScope.product.category.name}"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><c:out value="${requestScope.product.name}"/></li>
        </ol>
    </nav>
    <div class="row">
        <div class="col-lg-7">
            <div class="card my-4">
                <div class="card-header">
                    <h3><c:out value="${requestScope.product.name}"/></h3>
                </div>

                <div class="card-body">
                    <img class="card-img-top img-prev p-3" src="<c:out value="${requestScope.product.imageUrl}"/>" style="height: 400px; object-fit: scale-down;" alt="">
                </div>
                <div class="card-footer">
                    <h5 class="d-inline"><c:out value="${requestScope.product.price}"/> грн</h5>
                    <c:set var="productString" value="${requestScope.product.id += ';' += requestScope.product.name += ';' += requestScope.product.price}"/>
                    <button type="button" class="btn btn-info float-end add-to-cart" data-pr-info="<c:out value="${productString}"/>"><fmt:message key="addToCart"/></button></div>
            </div>
        </div>
        <div class="col-lg-5 my-4">
            <table class="table table-striped">
                <tbody>
                    <tr>
                        <th scope="row"><fmt:message key="brand"/></th>
                        <td><c:out value="${requestScope.product.brand}"/></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="w-100"></div>
        <div class="col-sm">
            <div class="card">
                <div class="card-body">
                    <c:out value="${requestScope.product.description}"/>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- /.container -->

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>

</html>
