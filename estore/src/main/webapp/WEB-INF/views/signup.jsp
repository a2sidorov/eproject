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
    <title>Sign up</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body onload="testPasswordComplexety()">
<%@ include file="partials/topnav.jsp" %>


<main id="signup-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1>Create an account</h1>

    <form:form action="/signup" method="post" modelAttribute="userSignupDto">

    <section id="personal-details-section">
    <h2>Personal details</h2>
    <ul>
        <li>
            <label for="firstName">First name</label>
            <form:input type="text" path="firstName"
                        placeholder="Enter your first name"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="firstName" cssClass="error" />
        </li>
        <li>
            <label for="lastName">Last name</label>
            <form:input type="text" path="lastName"
                        placeholder="Enter your last name"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="lastName" cssClass="error" />
        </li>
        <li>
            <label for="dateOfBirth">Date  of birth</label>
            <form:input type="date" path="dateOfBirth"
                        onfocusout="validate(this)"
                        required="required"/>
            <form:errors path="dateOfBirth" cssClass="error" />
        </li>
        <li>
            <label for="email">Email</label>
            <form:input type="email" path="email"
                        placeholder="Enter your email"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="email" cssClass="error" />
        </li>
        <li>
            <label for="password">Password</label>
            <div id="password-requirements">
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
                        placeholder="Enter your password"
                        onkeyup="testPasswordComplexety()"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="password" cssClass="error" />
        </li>
        <li>
            <label for="confirmPassword">Confirm Password</label>
            <form:input type="password" path="confirmPassword"
                        placeholder="Repeat the password"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="confirmPassword" cssClass="error" />
        </li>
    </ul>
    </section>

    <section id="shipping-details-section">
    <h2>Shipping Details</h2>
    <ul>
        <li>
            <label for="countryId">Country</label>
            <form:select path="countryId" required="required">
                <c:if test="${userSignupDto.countryId == null}">
                    <option selected="selected">Choose your country</option>
                </c:if>
                <c:forEach items="${countries}" var="country">
                    <c:if test="${country.id == userSignupDto.countryId}">
                        <option selected="selected" value="${country.id}">${country.name}</option>
                    </c:if>
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
            <div>
                <label for="house">House</label>
                <form:input type="text" path="house"
                            placeholder="House number"
                            onfocusout="validate(this)"
                            required="required" />
                <form:errors path="house" cssClass="error" />
            </div>
            <div>
                <label for="apartment">Apartment</label>
                <form:input type="text" path="apartment"
                            placeholder="Apt. number"
                            onfocusout="validate(this)"
                            required="required" />
                <form:errors path="apartment" cssClass="error" />

            </div>
        </li>
    </ul>
    </section>

    <button type="submit">Create my account</button>
    </form:form>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/signup.js"></script>
</body>
</html>
