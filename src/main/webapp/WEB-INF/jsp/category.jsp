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
                <li class="breadcrumb-item active" aria-current="page"><c:out
                        value="${requestScope.category.name}"/></li>
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
                <a class="btn btn-outline-primary dropdown-toggle" href="#" role="button" id="dropdownMenuLink"
                   data-bs-toggle="dropdown" aria-expanded="false">
                    <fmt:message key="sort"/>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_NEW_FIRST ? 'active' : ''}"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_NEW_FIRST}"/>">
                            <fmt:message key="newFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_CHEAP_FIRST ? 'active' : ''}"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_CHEAP_FIRST}"/>">
                            <fmt:message key="cheapFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_EXPENSIVE_FIRST ? 'active' : ''}"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_EXPENSIVE_FIRST}"/>">
                            <fmt:message key="expensiveFirst"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_A_Z ? 'active' : ''}"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_A_Z}"/>">
                            <fmt:message key="fromA"/>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item ${requestScope.sort eq Constants.SORT_Z_A ? 'active' : ''}"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoSort += '&' += 'sort=' += Constants.SORT_Z_A}"/>">
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
                        <a class="page-link"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoPage += '&' += 'page=' += requestScope.pageNumber - 1}"/>">
                            <fmt:message key="prev"/>
                        </a>
                    </li>
                    <li class="page-item disabled">
                        <p class="page-link disabled"><c:out value="${requestScope.pageNumber}"/></p>
                    </li>
                    <li class="page-item ${requestScope.nextPageExists ? '' : 'disabled'}">
                        <a class="page-link"
                           href="<c:out value="${Path.CATEGORY_PATH += '?' += requestScope.queryWithNoPage += '&' += 'page=' += requestScope.pageNumber + 1 }"/>">
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
            <form action="${Path.CATEGORY_PATH}" method="get" id="filter-form">
                <input type="hidden" name="id"
                       value="${requestScope.categoryId}" ${requestScope.categoryId == null ? 'disabled' : ''}>
                <input type="hidden" name="sort"
                       value="${requestScope.sort}" ${requestScope.sort == null ? 'disabled' : ''}>
                <div class="list-group">
                    <div class="list-group-item">
                        <div class="row my-2">
                            <div class="col-6">
                                <div class="form-floating">
                                    <input type="number" min="0" class="form-control" id="priceFrom" name="priceFrom" value="${requestScope.priceFrom}">
                                    <label for="priceFrom"><fmt:message key="priceFrom"/></label>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="form-floating">
                                    <input type="number" min="0" class="form-control" id="priceTo" name="priceTo" value="${requestScope.priceTo}">
                                    <label for="priceTo"><fmt:message key="priceTo"/></label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="list-group-item">
                        <fieldset>
                            <legend><fmt:message key="brand"/></legend>

                            <c:forEach items="${requestScope.allBrands}" var="brand">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="brand" value="${brand}" ${fn:contains(paramValues.brand, brand) ? 'checked' : ''}>
                                    <label class="form-check-label" >
                                            ${brand}
                                    </label>
                                </div>
                            </c:forEach>
                        </fieldset>
                    </div>
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
