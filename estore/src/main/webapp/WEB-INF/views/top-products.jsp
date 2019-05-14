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
    <title>Best-selling products</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>
<%@ include file="partials/reports-aside.jsp" %>


<main id="top-products-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1>Best-selling products</h1>

    <c:if test = "${products.size() eq 0}">
        <div class="nothingToShowMessage">Nothing is sold yet</div>
    </c:if>

    <c:if test = "${products.size() gt 0}">
        <table>
            <tr>
                <th></th>
                <th>Description</th>
                <th>Orders</th>
            </tr>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td class="product-image">
                        <a href="/product?id=${product.id}">
                            <img src="${product.imageUrl}" alt="No image uploaded">
                        </a>
                    </td>
                    <td class="product-info">${product.name}</td>
                    <td>${product.saleCount}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</main>
<%@ include file="partials/footer.jsp" %>

</body>
</html>
