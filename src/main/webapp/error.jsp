<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="error.css">
</head>
<body>
<div class="content">
    <h2>Some error happened!</h2>
    <h2><%=request.getAttribute("errorMessage")%>
    </h2>
    <h2>Back to main page: <a href="index.jsp">Main Page</a></h2>
    <h2>Back to login page: <a href="login.jsp">Login Page</a></h2>
</div>
</body>
</html>
