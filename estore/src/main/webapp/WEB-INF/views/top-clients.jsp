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
  ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  ~ SOFTWARE.
  --%>

<!DOCTYPE html>
<html>
<head>
    <title>Top clients</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>
<%@ include file="partials/reports-aside.jsp" %>


<main id="top-clients-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1>Top clients</h1>
    <c:if test = "${clients.size() eq 0}">
        <div class="nothingToShowMessage">No one bought anything yet.</div>
    </c:if>
    <c:if test = "${clients.size() gt 0}">
        <table>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Total</th>
            </tr>
            <c:forEach items="${clients}" var="client" varStatus="count">
                <tr>
                    <td>${client.firstName}</td>
                    <td>${client.lastName}</td>
                    <td>$${client.ordersSum}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</main>
<%@ include file="partials/footer.jsp" %>

</body>
</html>
