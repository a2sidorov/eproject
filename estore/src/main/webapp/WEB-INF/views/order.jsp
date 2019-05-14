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
    <title>Order</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body id="order-body">
<%@ include file="partials/topnav.jsp" %>

<main id="order-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>

<c:if test = "${orderProducts.size() eq 0}">
    <div class="nothingToShowMessage">Cart is empty</div>
</c:if>

<c:if test = "${orderProducts.size() gt 0}">
<table>
    <tr>
        <th></th>
        <th></th>
        <th>Price</th>
        <th>Qty</th>
        <th>Amount</th>
    </tr>
    <c:forEach items="${orderProducts}" var="op">
        <tr>
            <td class="product-image">
                <img src="${op.pk.product.imageUrl}" alt="">
                <div>In stock: <span>${op.pk.product.quantityInStock}</span> ${op.pk.product.measureUnits.name}</div>
            </td>
            <td class="product-info">${op.pk.product.name}</td>
            <td class="product-price">$${op.pk.product.sellingPrice}</td>
            <td class="product-quantity">
                <form:form action="/product/order/add" method="post" modelAttribute="productQuantityDto">
                    <form:input path="quantity" class="quantity"
                           type="text" name="quantity" value="${op.quantity}"
                           onkeyup="validateQuantity(this, ${op.pk.product.quantityInStock})"
                           onfocusout="updateQuantity(this, ${op.pk.product.id}, ${op.pk.product.quantityInStock})"
                           required="true" />
                    <form:button type="submit"></form:button>
                </form:form>
                <div class="remove-button">
                    <a href="/order/remove?productId=${op.product.id}">
                        <em class="material-icons">clear</em>
                    </a>
                </div>
            </td>
            <td class="product-total-price">
                <span>$</span><span>${op.calculateTotalSellingPrice()}</span>
            </td>
        </tr>
    </c:forEach>
    <tr class="table-product">
        <td></td>
        <td></td>
        <td></td>
        <td>Total:</td>
        <td id="orderPrice"><span>$</span><span>${order.calculateOrderPrice()}</span></td>
    </tr>
</table>
    <div>
        <a onclick="proceedToReserve()">Proceed to checkout</a>

    </div>
</c:if>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/order.js"></script>

</body>
</html>
