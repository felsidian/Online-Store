<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title><fmt:message key="${requestScope.product == null ? 'addProduct' : 'editProduct'}"/></title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <nav aria-label="breadcrumb" class="mt-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="#"><fmt:message key="home"/></a></li>
            <li class="breadcrumb-item active" aria-current="page"><fmt:message key="${requestScope.product == null ? 'addProduct' : 'editProduct'}"/></li>
        </ol>
    </nav>
    <div class="row">
        <div class="col">
            <h1 class="mb-4"><fmt:message key="${requestScope.product == null ? 'addProduct' : 'editProduct'}"/></h1>

            <c:choose>
                <c:when test="${requestScope.error != null}">
                    <div class="alert alert-danger" role="alert">
                         <c:out value="${requestScope.error}"/>
                    </div>
                </c:when>
            </c:choose>

            <form action="${Path.ADD_PRODUCT_PATH}" method="post" >
                <input type="hidden" name="id" value="${requestScope.product.id}">
                <div class="mb-4">
                    <label for="name" class="form-label"><fmt:message key="name"/></label>
                    <input type="text" class="form-control" placeholder="<fmt:message key="name"/>" id="name" name="name" required value="<c:out value="${requestScope.product.name}"/>">
                </div>
                <div class="mb-4">
                    <label for="category" class="form-label"><fmt:message key="category"/></label>
                    <select class="form-select" id="category" name="categoryId" aria-label="" required>
                        <c:forEach items="${requestScope.categories}" var="category">
                            <option value="${category.id}" ${requestScope.product.category.id == category.id ? ' selected' : ''}><c:out value="${category.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="row mb-4">
                    <div class="col-sm-4">
                        <label for="price" class="form-label"><fmt:message key="price"/></label>
                        <input type="number" class="form-control" placeholder="<fmt:message key="price"/>" id="price" name="price" min="1" step="any" required value="<c:out value="${requestScope.product.price}"/>">
                    </div>
                    <div class="col-sm-8">
                        <label for="brand" class="form-label"><fmt:message key="brand"/></label>
                        <input type="text" class="form-control" placeholder="<fmt:message key="brand"/>" id="brand" name="brand" value="<c:out value="${requestScope.product.brand}"/>">
                    </div>
                </div>
                <div class="mb-4">
                    <label for="description" class="form-label"><fmt:message key="description"/></label>
                    <textarea class="form-control" placeholder="<fmt:message key="description"/>" id="description" name="description" rows="5"><c:out value="${requestScope.product.description}"/></textarea>
                </div>
                <div class="mb-4">
                    <label for="imageUrl" class="form-label"><fmt:message key="imageUrl"/></label>
                    <input type="url" class="form-control" placeholder="<fmt:message key="imageUrl"/>" id="imageUrl" name="imageUrl" value="<c:out value="${requestScope.product.imageUrl}"/>">
                </div>
                <div class="row mb-4">
                    <div class="col-sm-4">
                        <label for="power" class="form-label"><fmt:message key="power"/></label>
                        <input type="number" min="1" step="any" class="form-control" placeholder="<fmt:message key="power"/>" id="power" name="power" value="<c:out value="${requestScope.product.power}"/>">
                    </div>
                    <div class="col-sm-4">
                        <label for="weight" class="form-label"><fmt:message key="weight"/></label>
                        <input type="number" min="1" step="any" class="form-control" placeholder="<fmt:message key="weight"/>" id="weight" name="weight" value="<c:out value="${requestScope.product.weight}"/>">
                    </div>
                    <div class="col-sm-4">
                        <label for="country" class="form-label"><fmt:message key="country"/></label>
                        <input type="text" class="form-control" placeholder="<fmt:message key="country"/>" id="country" name="country" value="<c:out value="${requestScope.product.country}"/>">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary mb-3"><fmt:message key="${requestScope.product == null ? 'addProduct' : 'editProduct'}"/></button>
            </form>
        </div>
    </div>

</div>
<!-- /.container -->

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>

</html>
