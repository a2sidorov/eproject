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
    <title>Profile</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="address-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
<h1>New Address</h1>
<form:form action="/profile/address/add" method="post" modelAttribute="userAddressDto">
    <ul>
        <li>
            <label for="countryId">Country</label>
            <form:select path="countryId" required="required">
                <option selected="selected">Choose your country</option>
                <c:forEach items="${countries}" var="country">
                    <option value="${country.id}">${country.name}</option>
                </c:forEach>
            </form:select>
        </li>
        <li>
            <label for="city">City</label>
            <form:input type="text" path="city"
                        placeholder="Enter your city name"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="city" cssClass="error" />
        </li>
        <li>
            <label for="postalCode">Postal code</label>
            <form:input type="text" path="postalCode"
                        placeholder="Enter your postal code"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="postalCode" cssClass="error" />
        </li>
        <li>
            <label for="street">Street</label>
            <form:input type="text" path="street"
                        placeholder="Enter your street name"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="street" cssClass="error" />
        </li>
        <li>
            <ul>
                <li>
                    <label for="house">House</label>
                    <form:input type="text" path="house"
                                placeholder="House number"
                                onfocusout="validate(this)"
                                required="required" />
                    <form:errors path="house" cssClass="error" />
                </li>
                <li>
                    <label for="apartment">Apartment</label>
                    <form:input type="text" path="apartment"
                                placeholder="Apt. number"
                                onfocusout="validate(this)"
                                required="required" />
                    <form:errors path="apartment" cssClass="error" />
                </li>
            </ul>
        </li>
    </ul>
    <button type="submit">Add address</button>
</form:form>
<a class="go-back-link" href="/profile">Go back</a>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/address.js"></script>
</body>
</html>