<!DOCTYPE html>
<%@ page import="jsp.Drop" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="branch" class="jsp.Drops" scope="request"/>
<jsp:useBean id="book" class="jsp.Book" scope="request"/>
<html>
<head>
    <%@ include file="includes/common.head.jsp" %>
    <title>Outline</title>
</head>
<body>
<%
    StringBuilder dropSB = new StringBuilder();
%>

<div class="p-3 mb-2 bg-info"></div>
<div class="container">
    <%
        if ( book.getXmlPath() == null || branch.drops.size() == 0) {
    %>
	
	<jsp:include page="includes/on.error.page.jsp">
        <jsp:param name="info" value="the book <span class='text-danger'>outline</span> delivery page"/>
		<jsp:param name="error" value="Something went wrong! Session probably expired."/>
		<jsp:param name="proposal" value="Start over from the main search page!"/>
    </jsp:include>

    <%
        }
        else
        {
            dropSB.append("[");

            for (Drop d : branch.drops) {
                out.println(d.getText());
                dropSB.append(d.getId()).append(",");
            }
            if (dropSB.charAt(dropSB.length() - 1) == ',') dropSB.setCharAt(dropSB.length() - 1, ']');
            else dropSB.append(']');
            dropSB.append(';');
        }
    %>
</div>
<div class="p-3 mt-5 bg-info"></div>

<%@ include file="includes/common.shoes.jsp" %>
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
