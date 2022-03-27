<%@ page import="jsp.Book" %>
<%@ page import="jsp.Edition" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="bookShelf" class="jsp.BookShelf" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>shes bya kun khyab yig rigs khag gnyis mnyam du bsgrigs pa</title>
</head>
<body>

<div class="jumbotron jumbotron-fluid">
    <div class="container">
        <h1 class="display-4">shes bya kun khyab yig rigs khag gnyis mnyam du bsgrigs pa</h1>
        <p class="lead">Параллельный поиск по нескольким синхронизированным текстам</p>
        <%--<hr class="my-4">--%>
        <%--<p>Пока что ищет только по коренному тексту (verses) "shes bya kun kyab" Джамгёна Конгтрула,--%>
        <%--синхронизированному с английсим его переводом – "The Encompassment of All Knowledge" (10 книг).</p>--%>
    </div>
</div>

<div class="container">

    <form action="" method="GET" target="_blank">

        <%
            String ul = "";
            out.println("<div class='book-list'>");
            for (Book book : bookShelf.getBooks()) {
                if(!ul.equals(book.getUL())) {
                    if(!ul.equals("")) out.println("</div></ul>");
                    ul = book.getUL();
                    out.println("<ul>");
                    out.println("<input class='form-check-input' type='checkbox'" + ((book.isChecked()) ? " checked/>" : "/>") );
                    out.println("<strong><a href='#' class='text-dark'>" + ul + "</a></strong>");
                    out.println("<div>");
                }
                out.println("<div class='form-check'>");
                out.print("<input class='form-check-input' type='checkbox' id='book" + book.getId()
                        + "' value='" + book.getId() + "' name='book'");
                if (book.isChecked()) out.print(" checked");
                out.println(">");
                out.print("<label class='form-check-label' for='book" + book.getId() + "'>");
                List<Edition> editions = book.getEditions();
                for (int i = 0; i < editions.size(); i++) {
                    if (i != 0) out.print(" / ");
                    out.print(editions.get(i).getTitle());
                }
                out.println("</label>");
                out.println("</div>");
            }
            if(!ul.equals("")) out.println("</div></ul>");
            out.println("</div>");
        %>
        <div class="row mt-3">
            <div class="col"></div>
            <div class="col alert alert-dark">
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" id="matchCase" name="case" value="1">
                    <label class="form-check-label" for="matchCase">Match Case</label>
                </div>
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" id="wholeWord" name="word" value="2">
                    <label class="form-check-label" for="wholeWord">Words</label>
                </div>
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" id="wylie" name="wylie" value="4" disabled>
                    <label class="form-check-label" for="wylie">Wylie</label>
                </div>
                <div class="form-check form-check-inline form-control-sm">
                    <input class="form-check-input" type="checkbox" id="regEx" name="regex" value="8">
                    <label class="form-check-label" for="regEx">RegEx</label>
                </div>
            </div>

            <div class="w-100"></div>

            <div class="col-2"></div>
            <div class="col alert alert-dark">
                <div class="input-group">
                    <input type="search" class="form-control rounded" placeholder="Search" aria-label="Search"
                           aria-describedby="search-addon" name="q"/>
                    <button type="submit" class="btn btn-outline-dark ml-2"><i class="fas fa-search"></i></button>
                </div>
            </div>
        </div>
    </form>

</div>

<div style="margin-top:500px;"></div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script defer src="https://use.fontawesome.com/releases/v5.0.13/js/all.js"></script>

<script>
    $(function () {

        function init() {
            $('ul > div')
                .hide()
                .addClass('bg-dark text-white')
                .css('position', 'absolute')
                .css('padding', 10)
                .css('overflow', 'hidden');
        }

        $('ul > input:checkbox').click(function() {
            $(this).siblings('div').first().find('input:checkbox').prop('checked', ($(this).is(':checked')));
        });

        $('ul > div > div > input:checkbox').click(function() {
            var omniTrue = true, omniFalse = false;
            $(this).closest('ul > div').find('div > input:checkbox').each(function () {
                var b = $(this).prop('checked');
                omniTrue &= b;
                omniFalse |= b;
            });
            var $menu = $(this).closest('ul').children('input:checkbox').first();

            $menu.prop('indeterminate', false);
            if ( omniTrue ) $menu.prop('checked', true);
            else if ( !omniFalse ) $menu.prop('checked', false);
            else $menu.prop('indeterminate', true);
        });

        $('ul a').click(function (e) {
            e.preventDefault();
            $d = $(this).closest('ul').children('div').first();
            if( $d.css('display') === "block" ) {
                $d.css("z-index", 1 );
            } else {
                $d.css("z-index", 1000 );
            }
            $d.toggle("fast");
        });

        $('ul').mouseleave(function (e) {
            $d = $(this).children('div').first();
            if( $d.css('display') === "block" ) {
                $d.toggle("fast").css("z-index", 1 );
            }
        });

        $('#wholeWord').click(function () {
            $('#wylie').prop("disabled", !($(this).prop("checked")));
        });

        $('form').on('submit', function (e) {
            var search = $("input[name='q']");
            var t = search.val().trim();
            var books = $('.book-list').find('input:checked');
            if (t.length === 0 || books.length === 0) {
                e.preventDefault();
            }
        });

        init();

    });
</script>
</body>
</html>
