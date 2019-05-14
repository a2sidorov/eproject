<%--
  ~ MIT License
  ~
  ~ Copyright (c) 2019 Andrei Sidorov
  ~
  ~ Permission is hereby granted, free of charge, to any person obtaining a copy
  ~ of this software and associated documentation files (the "Software"), to deal
  ~ in the Software without restriction, including without limitation the rights
  ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  ~ copies of the Software, and to permit persons to whom the Software is
  ~ furnished to do so, subject to the following conditions:
  ~
  ~ The above copyright notice and this permission notice shall be included in all
  ~ copies or substantial portions of the Software.
  ~
  ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  ~ SOFTWARE.
  --%>
<%@ include file="partials/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign in</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="signin-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
<h1>Sign in</h1>
<form:form class="auth-form" action="/signin" method="post">
    <ul>
        <li>
            <label>Email</label>
            <input type="email" name="username" required="required" value="admin@mail.dev"/>

        </li>
        <li>
            <label>Password</label>
            <input type="password" name="password" required="required" value="Password1"/>
        </li>
    </ul>
    <button type="submit">Submit</button>
</form:form>
<div class="recovery">
    <a href="#">Forgot password?</a><!--
 --><p>Need an account? <a href="/signup">Sign up</a></p>
</div>
</main>
<%@ include file="partials/footer.jsp" %>

</body>
</html>