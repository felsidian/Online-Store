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
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_NEW_FIRST ? 'active' : ''}" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_NEW_FIRST}"/>">
                            <fmt:message key="newFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_CHEAP_FIRST ? 'active' : ''}" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_CHEAP_FIRST}"/>">
                            <fmt:message key="cheapFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_EXPENSIVE_FIRST ? 'active' : ''}" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_EXPENSIVE_FIRST}"/>">
                            <fmt:message key="expensiveFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_A_Z ? 'active' : ''}" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_A_Z}"/>">
                            <fmt:message key="fromA"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_Z_A ? 'active' : ''}" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_Z_A}"/>">
                            <fmt:message key="fromZ"/>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- SORT -->

        <!-- PAGINATION -->
        <div class="col">
            <nav class="float-end">
                <ul class="pagination">
                    <li class="page-item ${requestScope.pageNumber > 1 ? '' : 'disabled' }">
                        <a class="page-link" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoPage += '&' += 'page=' += requestScope.pageNumber - 1}"/>">
                            <fmt:message key="prev"/>
                        </a>
                    </li>
                    <li class="page-item disabled">
                        <p class="page-link disabled"><c:out value="${requestScope.pageNumber}"/></p>
                    </li>
                    <li class="page-item ${requestScope.nextPageExists ? '' : 'disabled'}">
                        <a class="page-link" href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoPage += '&' += 'page=' += requestScope.pageNumber + 1 }"/>">
                            <fmt:message key="next"/>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <!-- PAGINATION -->

    </div>
    <div class="row">
        <div class="w-100"></div>

        <!-- FILTERS-->
        <div class="col-lg-3 col-md-5">
            <form action="category" method="get" id="filter-form">
                <input type="hidden" name="id" value="<c:out value="${param.id}"/>" ${param.id == null ? 'disabled' : ''}>
                <input type="hidden" name="sort" value="<c:out value="${param.sort}"/>" ${param.sort == null ? 'disabled' : ''}>
                <input type="hidden" name="page" value="<c:out value="${param.page}"/>" ${param.page == null ? 'disabled' : ''}>
                <div class="list-group">
                    <div class="input-group mb-3">
                        <span class="input-group-text"><fmt:message key="priceFrom"/></span>
                        <input type="text" class="form-control" name="price-from" aria-label="<fmt:message key="priceFrom"/>">
                        <span class="input-group-text"><fmt:message key="to"/></span>
                        <input type="text" class="form-control" name="price-to" aria-label="<fmt:message key="to"/>">
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
            </form>
        </div>

        <!-- FILTERS -->

        <div class="col-lg-9 col-md-7">

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
