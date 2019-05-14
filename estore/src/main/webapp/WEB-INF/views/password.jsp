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
    <title>New password</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="password-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
<h1>New password</h1>
<form:form action="/profile/password" method="post" modelAttribute="userPasswordDto">
    <ul>
        <li>
            <label for="currentPassword">Current password</label>
            <form:input type="password" path="currentPassword"
                        onfocus="showSubmitButton()"
                        placeholder="Enter your old password"
                        required="required" />
            <form:errors path="currentPassword" cssClass="error" />
        </li>
        <li>
            <label for="password">New password</label>
            <div>
                <ul>
                    <li>
                        <em id="lengthCheck" class="material-icons">clear</em>
                        <span>MUST contain at least 6 characters</span>
                    </li>
                    <li>
                        <em id="uppercaseCharCheck" class="material-icons">clear</em>
                        <span>MUST contain at least one uppercase character</span>
                    </li>
                    <li>
                        <em id="lowercaseCharCheck" class="material-icons">clear</em>
                        <span>MUST contain at least one uppercase character</span>
                    </li>
                    <li>
                        <em id="numberCheck" class="material-icons">clear</em>
                        <span>MUST contain at least one number</span>
                    </li>
                </ul>
            </div>
            <form:input type="password" path="password"
                        placeholder="Enter your new password"
                        onkeyup="testPasswordComplexety()"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="password" cssClass="error" />
        </li>
        <li>
            <label for="confirmPassword">Confirm new password</label>
            <form:input type="password" path="confirmPassword"
                        placeholder="Repeat the password"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="confirmPassword" cssClass="error" />
        </li>
    </ul>
    <button type="submit">Change my password</button>
</form:form>
<a class="go-back-link" href="/profile">Go back</a>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/password.js"></script>
</body>
</html>
