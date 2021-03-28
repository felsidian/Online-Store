<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><c:out value="${requestScope.category.name}"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <div class="col-lg-3">
        <nav aria-label="breadcrumb" class="mt-3">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
                <li class="breadcrumb-item active" aria-current="page"><c:out value="${requestScope.category.name}"/></li>
            </ol>
        </nav>
    </div>
    <h1 class="mb-3"><c:out value="${requestScope.category.name}"/></h1>
    <div class="row">
        <div class="col-lg-3">
        </div>

        <!-- SORT -->
        <div class="col">
            <div class="dropdown">
                <a class="btn btn-outline-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
                    <fmt:message key="sort"/>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <li><a class="dropdown-item" href="#"><fmt:message key="cheapFirst"/></a></li>
                    <li><a class="dropdown-item" href="#"><fmt:message key="expensiveFirst"/></a></li>
                    <li><a class="dropdown-item" href="#"><fmt:message key="fromA"/></a></li>
                    <li><a class="dropdown-item" href="#"><fmt:message key="fromZ"/></a></li>
                </ul>
            </div>
        </div>
        <!-- SORT -->

        <!-- PAGINATION -->
        <div class="col">
            <nav class="float-end">
                <ul class="pagination">
                    <li class="page-item disabled">
                        <a class="page-link" href="#"><fmt:message key="prev"/></a>
                    </li>
                    <li class="page-item disabled">
                        <p class="page-link disabled" >1</p>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="#"><fmt:message key="next"/></a>
                    </li>
                </ul>
            </nav>
        </div>
        <!-- PAGINATION -->
    </div>
    <div class="row">
        <div class="w-100"></div>

        <!-- FILTERS-->
        <div class="col-lg-3">
            <div class="list-group">
                <div class="input-group mb-3">
                    <span class="input-group-text"><fmt:message key="priceFrom"/></span>
                    <input type="text" class="form-control" aria-label="<fmt:message key="priceFrom"/>">
                    <span class="input-group-text"><fmt:message key="to"/></span>
                    <input type="text" class="form-control" aria-label="<fmt:message key="to"/>">
                    <span class="input-group-text"><fmt:message key="uah"/></span>
                </div>
                <%--<div class="card mb-3">
                    <div class="card-header">
                        Source of power
                    </div>
                    <div class="card-body">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked" checked="">
                            <label class="form-check-label" for="flexCheckChecked">
                                Battery
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">
                            <label class="form-check-label" for="flexCheckDefault">
                                Grid
                            </label>
                        </div>
                    </div>
                </div>--%>
                <button type="submit" class="btn btn-secondary my-3"><fmt:message key="filter"/></button>
            </div>

        </div>
        <!-- FILTERS -->

        <div class="col-lg-9">

            <div class="row">
                <c:forEach items="${requestScope.products}" var="product">
                    <div class="col-lg-4 col-md-6 mb-4">
                        <%@include file="/WEB-INF/jspf/product-card.jspf" %>
                    </div>
                </c:forEach>


            </div>
            <!-- /.row -->

        </div>
        <!-- /.col-lg-9 -->

    </div>
    <!-- /.row -->

</div>
<!-- /.container -->

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>

</html>
