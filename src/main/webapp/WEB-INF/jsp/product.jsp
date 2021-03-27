<%@include file="/WEB-INF/jspf/header.jspf" %>

<head>
    <meta name="description" content="">
    <title>Shop Homepage - Start Bootstrap Template</title>
    <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>

<body class="d-flex flex-column h-100" onload="onPageLoad()">

<%@include file="/WEB-INF/jspf/navbar.jspf" %>

<!-- Page Content -->
<div class="container">
    <div class="row">
        <div class="col-lg">
            <div class="card my-4">
                <div class="card-header">
                    <h3>Product 1</h3>
                </div>
                <img class="card-img-top" src="http://placehold.it/900x600" alt="Card image cap">
                <div class="card-body">
                    <div class="row " >
                        <div class="col-sm">
                            <h5 class="card-title">1234 грн</h5>
                        </div>
                        <div class="col-sm">
                            <a href="#" class="btn btn-primary float-right">Add to cart</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg">
            <h4 class="my-4">Характеристики</h4>
            <table class="table table-striped">
                <tbody>
                <tr>
                    <th scope="row">1йцуйцуйцу</th>
                    <td>Markфывфывфывфы</td>
                </tr>
                <tr>
                    <th scope="row">2йцуйцуйцуйцу</th>
                    <td>Jacobфывфывфыв</td>
                </tr>
                <tr>
                    <th scope="row">3йцуйцуйцуйцу</th>
                    <td>Larryфывфывфыв</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="w-100"></div>
        <div class="col-sm">
            <div class="card">
                <div class="card-body">
                    Електролобзик Metabo STEB 65 Quick 450 Вт має особливості:<br>
                    Дугова ручка з нековзною накладкою Softgrip<br>
                    Metabo Quick для заміни пиляльного полотна без інструментів<br>
                    Електроніка Vario (V) для керування частотою ходів залежно від матеріалу<br>
                    Можливість видалення пилу завдяки під'єднанню пристрою для видалення пилу<br>
                    Функція здування тирси для вільного огляду місця оброблення<br>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- /.container -->

<%@include file="/WEB-INF/jspf/footer.jspf" %>
</body>

</html>
