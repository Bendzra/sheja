<%@ page import="jsp.Drop" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="drops" class="jsp.Drops" scope="request"/>
<jsp:useBean id="book" class="jsp.Book" scope="request"/>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <%--<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">--%>
    <title>Outline</title>
</head>
<body>
<%!
    private StringBuilder dropSB;
%>

<div class="p-3 mb-2 bg-info"></div>
<div class="container">
    <%
        dropSB = new StringBuilder();
        dropSB.append("[");

        for (Drop d : drops.drops) {
            out.println(d.getText());
            dropSB.append(d.getId()).append(",");
        }
        if (dropSB.charAt(dropSB.length() - 1) == ',') dropSB.setCharAt(dropSB.length() - 1, ']');
        else dropSB.append(']');
        dropSB.append(';');
    %>
</div>
<div class="p-3 mt-5 bg-info"></div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script>
    $(document).ready(function () {
        var crumb_id = -1;
        var drop_id = -1;

        function identify($crumb) {
            <%
                out.print("var drops = "); out.println(dropSB);
            %>
            crumb_id = $('li').index($crumb.closest('li')) + 1;
            drop_id = drops[crumb_id - 1];
        }

        $('a').on('click', function() {
            identify($(this));
            $(this).attr("href",
                "${pageContext.request.contextPath}/show?book_id=" + ${book.id}
                + "&crumb_id=" + crumb_id
                + "&drop_id=" + drop_id);
            $(this).attr("target", "_new");
        });
    });
</script>

</body>
</html>
