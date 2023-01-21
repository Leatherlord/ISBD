<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration Page</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
<div class="content">
    <form class="form" method="post" action="${pageContext.request.contextPath}/registration">
        <label for="login">Login:</label>
        <input id="login" type="text" name="login">
        <label for="password">Password:</label>
        <input id="password" type="password" name="password">
        <button class="ui-button" name="test_button" type="submit">Register</button>
    </form>
    <h2>Log in page: <a href="login.jsp">Log in</a></h2>
</div>
</body>
</html>
