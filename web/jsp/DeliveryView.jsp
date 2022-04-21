<!DOCTYPE html>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="jsp.Drop" %>
<%@ page import="java.util.List" %>
<%@ page import="jsp.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="results" class="jsp.Found" scope="request"/>
<jsp:useBean id="book" class="jsp.Book" scope="request"/>
<jsp:useBean id="ofInterest" class="jsp.BookShelf" scope="request"/>
<%!
    String getFormattedDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return sdf.format(new Date());
    }
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <title>Found</title>
</head>
<body>

<%!
    private StringBuilder crumbSB;
    private StringBuilder dropSB;
%>

<div class="container">
    <%
        if (results.getPairs().size() == 0) {
    %>
    <h1>Welcome to</h1>
    <p>the search results delivery page</p>
    <div class="p-3 mb-2 bg-info text-white">No entries found</div>
    <i>Today is <%= getFormattedDate() %>
    </i>
    <%
    } else {
    %>
    <div class='p-3 mb-2 bg-success text-white'>${results.pairs.size()} entries found</div>
    <%
        out.println("<div class='d-flex justify-content-center align-items-center my-5'>");
        for(Book b: ofInterest.getBooks()) {
            if( b.getId() == book.getId()) out.print("<a href='#' class='btn btn-primary ofInterest'>" + b.getId() + "</a> ");
            else  out.print("<a href='#' class='btn ofInterest'>" + b.getId() + "</a> ");
        }
        out.println("</div>");

        crumbSB = new StringBuilder();
        crumbSB.append("[");

        dropSB = new StringBuilder();
        dropSB.append("[");

        for (Pair<Pair<Drop, String>, Pair<Drop, String>> p : results.getPairs()) {

            Drop drop = p.getKey().getKey();
            List<Pair<Integer, String>> chapters = drop.getChapters();

            dropSB.append(drop.getId()).append(",");

            out.println("<div class='row'>");
            out.println("<div class='col'>");

            out.print("<small><a href='#' class='edition'>");
            out.print(drop.getEdition().getTitle());
            out.print("</a></small>");

            out.print("<small class='drop'>");
            crumbSB.append('[');
            for (int i = 0; i < chapters.size(); i++) {
                if (i == 0) out.print("<br />");

                if (i == 0) crumbSB.append(chapters.get(i).getKey());
                else {
                    crumbSB.append(",");
                    crumbSB.append(chapters.get(i).getKey());
                }
                out.print("&gt;<a href='#' class='text-secondary px-1 crumb'>");
                out.print(chapters.get(i).getValue() + "</a>");
            }
            crumbSB.append("],");

            out.println(":</small>");
            out.println("</div>");

            drop = p.getValue().getKey();
            chapters = drop.getChapters();

            dropSB.append(drop.getId()).append(",");

            out.println("<div class='col'>");

            out.print("<small><a href='#' class='edition'>");
            out.print(drop.getEdition().getTitle());
            out.print("</a></small>");

            out.print("<small class='drop'>");
            for (int i = 0; i < chapters.size(); i++) {
                if (i == 0) out.print("<br />");
                out.print("&gt;<a href='#' class='text-secondary px-1 crumb'>");
                out.print(chapters.get(i).getValue() + "</a>");
            }
            out.println(":</small>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class='row border-bottom align-items-center py-4'>");
            out.println("<div class='col'>" + p.getKey().getValue() + "</div>");
            out.println("<div class='col'>" + p.getValue().getValue() + "</div>");
            out.println("</div>");
        }
        if (crumbSB.charAt(crumbSB.length() - 1) == ',') crumbSB.setCharAt(crumbSB.length() - 1, ']');
        else crumbSB.append(']');
        crumbSB.append(';');
        if (dropSB.charAt(dropSB.length() - 1) == ',') dropSB.setCharAt(dropSB.length() - 1, ']');
        else dropSB.append(']');
        dropSB.append(';');
    %>
    <%
        out.println("<div class='d-flex justify-content-center align-items-center my-5'>");
        for(Book b: ofInterest.getBooks()) {
            if( b.getId() == book.getId()) out.print("<a href='#' class='btn btn-primary ofInterest'>" + b.getId() + "</a> ");
            else  out.print("<a href='#' class='btn ofInterest'>" + b.getId() + "</a> ");
        }
        out.println("</div>");
    %>
    <div class='p-3 mb-2 bg-success text-white'></div>
    <%
        }
    %>
</div>


</form>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<%--<script defer src="https://use.fontawesome.com/releases/v5.0.13/js/all.js"></script>--%>

<script>
    $(document).ready(function () {

        $('.toggleRight').on('click', function (e) {
            var row = $(this).closest('div.row');
            row.find(".collapse._hidden").collapse('toggle');
            var rights = row.find(".toggleRight");
            rights.each(function () {
                $(this).children('.fa').toggleClass("fa-chevron-down fa-chevron-up");
            });
        });

        $('.toggleLeft').on('click', function (e) {
            var row = $(this).closest('div.row');
            row.find(".collapse.hidden_").collapse('toggle');
            var lefts = row.find(".toggleLeft");
            lefts.each(function () {
                $(this).children('.fa').toggleClass("fa-chevron-down fa-chevron-up");
            });
        });

        var crumb_id = -1;
        var drop_id = -1;

        function identify($crumb) {
            <%
                out.print("var crumbs = "); out.println(crumbSB);
                out.print("var drops = "); out.println(dropSB);
            %>
            var step_away = $crumb.index() - 1;
            var drop_index = $('.drop').index($crumb.closest('.drop'));
            drop_id = drops[drop_index];

            var drops_per_row = drops.length / crumbs.length | 0;
            var fuse_index = drop_index / drops_per_row | 0;
            crumb_id = crumbs[fuse_index][step_away];
        }

        $('.edition').on('click', function() {
            var $drop = $(this).closest('small').next();
            var $crumb = $drop.children().last();

            identify($crumb);
            $(this).attr("href",
                "${pageContext.request.contextPath}/outline?book_id=" + ${book.id}
                + "&crumb_id=" + crumb_id
                + "&drop_id=" + drop_id);
            $(this).attr("target", "_new");
        });

        $('.crumb').on('click', function() {
            identify($(this));
            $(this).attr("href",
                "${pageContext.request.contextPath}/show?book_id=" + ${book.id}
                + "&crumb_id=" + crumb_id
                + "&drop_id=" + drop_id);
            $(this).attr("target", "_new");
        });

        $('.ofInterest').on('click', function() {
            <%
                String qString = request.getQueryString();
            %>
            var qs = "<% out.print(qString); %>";
            qs = qs.replace(/&?book=\d+\b/g, "");

            var s = "";
            $(this).parent().children().each(function( i ) {
                if (i != 0) s += "&";
                s += "book=" + $( this ).text();
            });
            if (qs.length > 0 && qs.charAt(0) !== '&') s += '&';
            qs = s + qs;

            re = /\bcb=\d+\b/;
            s = "cb=" + $(this).text();
            if(re.test(qs)) {
                qs = qs.replace(re, s);
            } else {
                qs += "&" + s;
            }
            $(this).attr("href", "${pageContext.request.contextPath}/sheja?" + qs );
            $(this).attr("target", "_self");
        });


    });
</script>

</body>
</html>
