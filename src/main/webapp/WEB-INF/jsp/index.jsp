<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Open mHealth Web Application</title>
</head>
<body>
<h1>Welcome to our Open mHealth Test web application</h1>
<a href="/greeting">
    <button> Greetings</button>
</a>

<a href="/shimmer">
    <button> get shimmer data</button>
</a>

<form:form method="POST" action="/create_user" modelAttribute="user">
    <table>
        <tr>
            <td>
                <form:label path="username">Username</form:label>
            </td>
            <td>
                <form:input path="username"/>
            </td>
        </tr>
        <tr>
            <td>
                <form:label path="password">Password</form:label>
            </td>
            <td>
                <form:input path="password"/>
            </td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>


<form method="POST" action="GetDataServlet">
    <input type="submit" name="getData" value="Get Data!"/>
</form>
<a href="/VisualizeData">
    <button> Visuzalize data!</button>
</a>

</body>
</html>