<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title>Leet Tools</title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container my-4">

    <div class="row">

        <div class="col-lg-3">

            <div class="list-group">
                <c:forEach items="${requestScope.categories}" var="category">
                    <a href="<c:out value="${Path.CATEGORY_PATH += '?id=' += category.id}"/>" class="list-group-item"><c:out value="${category.name}"/></a>
                </c:forEach>
            </div>

        </div>
        <!-- /.col-lg-3 -->

        <div class="col-lg-9">

            <img src="http://openchina.com.ua/wp-content/uploads/Dawer-catalogue1.jpg" class="d-block w-100 mb-4" alt="..." style="height: 350px; object-fit: scale-down;">

            <!-- products -->
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
