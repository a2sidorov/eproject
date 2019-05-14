<%@ include file="partials/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<h1>Admin page</h1>
<sec:authorize access="!isAuthenticated()">
    Not authenticated
</sec:authorize>

<sec:authorize access="isAuthenticated()">
    Authenticated
</sec:authorize>

<sec:authorize access="hasAuthority('USER')">
    <p>Your role is USER</p>
</sec:authorize>

<sec:authorize access="hasAuthority('ADMIN')">
    <p>Your role is ADMIN</p>
</sec:authorize>



</body>
</html>