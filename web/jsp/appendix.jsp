<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%
    String appendix = request.getParameter("a");
    if(appendix.equals("glossary")) {
%>
<%@ include file="includes/sheja-appendix.combined.glossary.htm" %>
<%
    } else if (appendix.equals("names")) {
%>
<%@ include file="includes/sheja-appendix.combined.names.htm" %>
<%
    }
%>
