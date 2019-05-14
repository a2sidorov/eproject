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
    <title>Payment</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body onload="startTimer(${productReserveTime}, ${secondsPassed})">
<%@ include file="partials/topnav.jsp" %>

<main id="checkout-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <div>Time left: <span id="timer"></span> </div>
    <h1>Checkout</h1>
    <form:form action="/checkout" method="post" modelAttribute="checkoutDto">

    <section id="payment-method-section">
    <h2>Payment method</h2>
        <ul id="payment-options">
            <li>
                <form:radiobutton path="paymentMethod" value="CASH" onclick="hideCardInputFields()"
                                  checked="checked" />
                <span>Cash</span>
            </li>
            <li>
                <form:radiobutton path="paymentMethod" value="CARD" onclick="showCardInputFields()" />
                <span>Credit/Debit/Prepaid card</span>
            </li>
        </ul>
        <ul id="card-info">
            <li>
                <label for="cardNumber">Card number</label>
                <form:input type="text" path="cardNumber" value="0000000000000000"
                            placeholder="Enter your card number"
                            onfocusout="validate(this)"
                            required="required" />
                <form:errors path="cardNumber" cssClass="error" />
            </li>
            <li>
                <label for="nameOnCard">Name on card</label>
                <form:input type="text" path="nameOnCard" value="not entered"
                            placeholder="Enter name as on the card"
                            onfocusout="validate(this)"
                            required="required" />
                <form:errors path="nameOnCard" cssClass="error" />
            </li>
            <li>
                <ul>
                    <li>
                        <label for="expirationMonth">Expiration date</label>
                        <div>
                            <form:select path="expirationMonth" required="required">
                                <option value="1">1 - Jan</option>
                                <option value="2">2 - Feb</option>
                                <option value="3">3 - Mar</option>
                                <option value="4">4 - Apr</option>
                                <option value="5">5 - May</option>
                                <option value="6">6 - Jun</option>
                                <option value="7">7 - Jul</option>
                                <option value="8">8 - Aug</option>
                                <option value="9">9 - Sep</option>
                                <option value="10">10 - Oct</option>
                                <option value="11">11 - Nov</option>
                                <option value="12">12 - Dec</option>
                            </form:select>
                            <form:select path="expirationYear" required="required">
                                <c:forEach items="${expirationYears}" var="expirationYear">
                                    <option value="${expirationYear}">${expirationYear}</option>
                                </c:forEach>
                            </form:select>
                            <form:errors path="expirationYear" cssClass="error" />
                        </div>
                    </li>
                    <li>
                        <label for="CVV">CVV</label>
                        <form:input type="text" path="CVV" value="000"
                                    placeholder="ex. 311"
                                    onfocusout="validate(this)"
                                    required="required" />
                        <form:errors path="CVV" cssClass="error" />
                    </li>
                </ul>
            </li>
        </ul>
    </section>

    <section id="shipping-info-section">
    <h2>Shipping information</h2>
        <ul>
            <li>
                <label for="addressId">Address</label>
                <form:select path="addressId" required="required">
                    <c:forEach items="${addresses}" var="address">
                        <option value="${address.id}">${address.house} ${address.street}
                                apt. ${address.apartment}
                                ${address.country.name}, ${address.city}
                                ${address.postalCode}
                                </option>
                    </c:forEach>
                </form:select>
            </li>
        </ul>
        <ul id="delivery-methods">
            <li>
                <form:radiobutton path="shippingMethod" value="STANDARD"
                                  checked="checked" />
                <span>Standard</span>
            </li>
            <li>
                <form:radiobutton path="shippingMethod" value="EXPRESS" />
                <span>Express</span>
            </li>
        </ul>
    </section>
    <button type="submit">Check out</button>
    </form:form>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/checkout.js"></script>
</body>
</html>


