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
    <title>Home</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body onload="startTimer(${productReserveTime}, ${secondsPassed})">
<%@ include file="partials/topnav.jsp" %>

<main id="order-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
<div>Time left: <span id="timer"></span> </div>
<table>
    <tr>
        <th></th>
        <th></th>
        <th>Price</th>
        <th>Qty</th>
        <th>Amount</th>
    </tr>
    <c:forEach items="${orderProducts}" var="orderProduct">
        <tr>
            <td class="product-image">
                <img src="${orderProduct.pk.product.imageUrl}" alt="">
                <div>In stock: <span>${orderProduct.pk.product.quantityInStock}</span>
                        ${orderProduct.pk.product.measureUnits.name}
                </div>
            </td>
            <td class="product-info">${orderProduct.pk.product.name}</td>
            <td class="product-price">$${orderProduct.pk.product.sellingPrice}</td>
            <td class="product-quantity">${orderProduct.quantity} </td>
            <td class="product-total-price">
                <span>$</span><span>${orderProduct.calculateTotalSellingPrice()}</span>
            </td>
        </tr>
    </c:forEach>
    <tr class="table-product">
        <td></td>
        <td></td>
        <td></td>
        <td>Total price:</td>
        <td id="totalPrice">$${order.calculateOrderPrice()}</td>
    </tr>
</table>
<div>
    <a href="/checkout/payment">Proceed to payment</a>
</div>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/reserve.js"></script>
</body>
</html>