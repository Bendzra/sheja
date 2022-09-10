<%@ page import="jsp.Book" %>
<%@ page import="jsp.Edition" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="bookShelf" class="jsp.BookShelf" scope="request"/>
<html>
<head>
    <%@ include file="includes/common.head.jsp" %>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/shejaView.css" />
    <title>shes bya kun khyab yig rigs khag gnyis mnyam du bsgrigs pa</title>

    <script>
        function provideRows(str)
        {
            var tbody = document.getElementById("appendix");
            if (str.length === 0) {
                tbody.innerHTML = "";
            } else {
                var xml = new XMLHttpRequest();
                xml.onreadystatechange = function () {
                    if (this.readyState === 4 && this.status === 200) {
                        tbody.innerHTML = this.responseText;
                        tbody.parentElement.parentElement.classList.remove("d-none");
                    }
                };
                xml.open("GET", "<%=request.getContextPath()%>/jsp/appendix.jsp?a=" + str, false);
                xml.send();
            }
        }
    </script>

</head>
<body>
<div class="jumbotron jumbotron-fluid">
    <div class="container">
        <h1 class="display-4">shes bya kun khyab yig rigs khag gnyis mnyam du bsgrigs pa</h1>
        <hr class="my-2" />
        <%
            String[] epithets = new String[] {"concurrent", "linked", "synchronized", "concordant", "parallel"};
            int s = 1 + (int) ( (epithets.length - 1) * Math.random());
            int t = (int) ( (epithets.length - 1) * Math.random() );
            while( s == t) {
                t = (int) ( (epithets.length - 1) * Math.random() );
            }
            out.println("<p class=\"lead\">" + epithets[s] + " search across multiple " + epithets[t] + " texts</p>");
        %>
		<%--<p class="lead">Concordant search across multiple linked texts</p>--%>
        <%--<p class="lead">Параллельный поиск по нескольким синхронизированным текстам</p>--%>
        <%--<hr class="my-4">--%>
        <%--<p>Ищет по текстам Энциклопедии "shes bya kun kyab" Джамгёна Конгтрула,--%>
        <%--синхронизированным с английским её переводом – "The Encompassment of All Knowledge" (10 книг).</p>--%>
    </div>
</div>

<div class="while-progress d-none">
    <%--<i class="fas fa-2x fa-spinner fa-spin"></i>--%>
	<i class="fa fa-2x fa-spinner fa-spin"></i>
    <label>Message...</label>
</div>

<div class="container">

    <form action="" method="GET" target="_blank">
        <%
            String ul = null;
            out.println("<div class='book-list'>");
            for (Book book : bookShelf.getBooks())
            {
                if(!book.getUL().equals(ul))
                {
                    // completing the previous <ul> if it was started:
                    if(ul != null) out.println("</div></ul>");

                    // starting new <ul>:
                    ul = book.getUL();
                    out.println("<ul class='mb-2'>");
                    // the actual checked status of this input is set in JS
                    out.println("<input class='form-check-input' type='checkbox'/>");
                    out.println("<strong><a href='#' class='text-dark'>" + ul + "</a></strong>");
                    out.println("<div>");
                }
                out.println("<div class='form-check'>");
                out.print("<input class='form-check-input' type='checkbox' id='book" + book.getId()
                        + "' value='" + book.getId() + "' name='v'");
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
            // completing the last <ul> if it was started:
            if(ul != null) out.println("</div></ul>");

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
                    <button type="submit" class="btn btn-outline-dark ml-2"><i class="fa fa-search"></i></button>
                </div>
            </div>
        </div>
    </form>

    <nav class="navbar navbar-expand-sm navbar-light bg-light">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="">Glossary</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="">Names</a>
            </li>
            <li class="nav-item disabled">
                <a class="nav-link disabled" href="">Indexes</a>
            </li>
        </ul>
    </nav>

	<div class="table-responsive h-50 d-none">
		<table class="table table-sm table-striped">
            <thead class="thead-dark sticky-top" style="z-index:1;">
                <tr>
                    <th class="d-none">id</th>
                    <th>Tibetan</th>
                    <th>Sanskrit</th>
                    <th>English</th>
                    <th class="d-none">book</th>
                    <th class="d-none">comment</th>
                </tr>
            </thead>
            <tbody id="appendix">
                <!--rows to insert-->
            </tbody>
        </table>
	</div>
</div>

<%@ include file="includes/common.shoes.jsp" %>
<script src="<%=request.getContextPath()%>/js/shejaView.js"></script>

</body>
</html>
