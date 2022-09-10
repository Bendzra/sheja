<!DOCTYPE html>
<%@ page import="jsp.Drop" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="branch" class="jsp.Drops" scope="request"/>
<html>
<head>
    <%@ include file="includes/common.head.jsp" %>
    <title>Show</title>
</head>
<body>
<div class="p-3 mb-2 bg-info"></div>
<div class="container">
    <%
        if ( branch.drops.size() == 0 ) {
    %>
    
	<jsp:include page="includes/on.error.page.jsp">
        <jsp:param name="info" value="the book <span class='text-danger'>section</span> delivery page"/>
		<jsp:param name="error" value="Something went wrong! Session probably expired."/>
		<jsp:param name="proposal" value="Start over from the main search page!"/>
    </jsp:include>

    <%
        }
        else
        {
            for (Drop d : branch.drops) {
                out.println(d.getText());
            }
        }
    %>
</div>
<div class="p-3 mt-5 bg-info"></div>

<%@ include file="includes/common.shoes.jsp" %>

</body>
</html>
