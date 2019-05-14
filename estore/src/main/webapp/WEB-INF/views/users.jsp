<%@ include file="partials/taglibs.jsp" %>
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
  ~  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  ~ SOFTWARE.
  ~
  --%>

<!DOCTYPE html>
<html>
<head>
    <title>Users</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="users-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1>Users</h1>
    <table id="users-table">

        <!-- Header -->
        <tr>
            <th>First name</th>
            <th>Last name</th>
            <th>Email</th>
            <th>Role</th>
            <th></th>
        </tr>

        <!-- Search bar -->
        <form:form action="/users/find" method="post" modelAttribute="searchUsersDto" >
            <tr>
                <td>
                    <form:input type="text" path="firstName" onfocusout="validate(this)" />
                </td>
                <td>
                    <form:input type="text" path="lastName" onfocusout="validate(this)" />
                </td>
                <td>
                    <form:input type="text" path="email" onfocusout="validate(this)" />
                </td>
                <td>
                    <form:select path="roleId">
                        <c:forEach items="${roles}" var="role">
                            <c:choose>
                                <c:when test="${role.id == searchUsersDto.roleId}">
                                    <option selected="selected" value="${role.id}">${role.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${role.id}">${role.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${searchUsersDto.roleId == 0}">
                                <option selected="selected" value="0">ALL</option>
                            </c:when>
                            <c:otherwise>
                                <option value="0">ALL</option>
                            </c:otherwise>
                        </c:choose>
                    </form:select>
                </td>
                <td>
                    <%--
                    <form:button id="findButton">Find</form:button>
                    --%>
                    <form:button type="submit">Find</form:button>
                </td>
            </tr>
        </form:form>

        <!-- Table content -->
        <c:forEach items="${users}" var="user">
            <tr class="users-row">
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>
                    <form action="#">
                    <c:forEach items="${roles}" var="role">

                    <c:forEach items="${user.roles}" var="userRole">
                        <c:if test="${role.id == userRole.id}">
                            <c:set var="contains" value="true"/>
                        </c:if>
                    </c:forEach>
                        <c:choose>
                            <c:when test="${contains == 'true'}">
                                <div class="user-role">
                                    <input type="checkbox"
                                           onclick="updateUserRoles(this, '${user.id}', '${role.id}')"
                                           name="${role.id}" checked="checked">
                                    <label>${role.name}</label>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="user-role">
                                    <input type="checkbox"
                                           onclick="updateUserRoles(this, '${user.id}', '${role.id}')"
                                           name="${role.id}">
                                    <label>${role.name}</label>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <c:set var="contains" value="false"/>

                    </c:forEach>
                    </form>
                </td>
                <td></td>
            </tr>
        </c:forEach>
    </table>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/users.js"></script>

</body>
</html>
