<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%!
    String getFormattedDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        return sdf.format(new Date());
    }
%>
<div class="p-3 mb-2"></div>
<h1>Welcome to</h1>
<p>${param.info}</p>
<div class="p-3 mb-2 bg-info text-white">${param.error}</div>
<p><i>${param.proposal}</i></p>
<i>Today is <%= getFormattedDate() %></i>
<div class="p-3 mt-5"></div>
