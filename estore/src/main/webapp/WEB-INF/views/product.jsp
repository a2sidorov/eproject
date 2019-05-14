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
    <title>Product</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="product-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>

    <c:if test = "${empty product}">
        <div class="nothingToShowMessage">This product does not exist</div>
    </c:if>

    <c:if test = "${not empty product}">
        <div id="product-image"><img src="${product.imageUrl}" alt="No image uploaded"></div>
        <div>${product.name}</div>

        <section class="product-details-section">
            <h2>Product attributes</h2>
            <table>
                <c:forEach items="${attributes}" var="attribute">
                    <tr>
                        <td>${attribute.pk.attribute.name}:</td>
                        <td>${attribute.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </section>

        <section class="product-details-section">
            <h2>Additional details</h2>
            <table>
                <tr>
                    <td>Height:</td>
                    <td>${product.height} meters</td>
                </tr>
                <tr>
                    <td>Width:</td>
                    <td>${product.width} meters</td>
                </tr>
                <tr>
                    <td>Depth:</td>
                    <td>${product.depth} meters</td>
                </tr>
                <tr>
                    <td>Weight:</td>
                    <td>${product.weight} kilograms</td>
                </tr>
            </table>
        </section>

        <sec:authorize access="hasAuthority('ROLE_MANAGER')">
            <a href="/product/${product.id}/edit">Edit product</a>
        </sec:authorize>
    </c:if>

</main>
<%@ include file="partials/footer.jsp" %>
</body>
</html>