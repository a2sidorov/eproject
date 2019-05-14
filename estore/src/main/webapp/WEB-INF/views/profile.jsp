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

<div id="profile-side-nav">
    <ul>
        <li><a href="/profile/address/add">Add address</a></li>
        <li><a href="/profile/password">Change password</a></li>
    </ul>
</div>

<main id="profile-main">
<c:if test="${not empty message}"><div id="message">${message}</div></c:if>
<c:if test="${not empty error}"><div id="error">${error}</div></c:if>
<h1>Profile</h1>

<section id="personal-details-section">
<h2>Personal details</h2>
    <form:form action="/profile/personal-details/update" method="post" modelAttribute="userDetailsDto">
    <ul>
        <li>
            <label for="firstName">First name</label>
            <form:input type="text" path="firstName"
                        data-currentvalue="${user.firstName}"
                        value="${user.firstName}"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="firstName" cssclass="error" />
        </li>
        <li>
            <label for="lastName">Last name</label>
            <form:input type="text" path="lastName"
                        data-currentvalue="${user.lastName}"
                        value="${user.lastName}"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="lastName" cssclass="error" />
        </li>
        <li>
            <label for="dateOfBirth">Date of birth</label>
                <form:input type="date" path="dateOfBirth"
                            data-currentvalue="${user.dateOfBirth}"
                            value="${user.dateOfBirth}"
                            onfocusout="validate(this)"
                            required="required"/>
            <form:errors path="dateOfBirth" cssclass="error" />
        </li>
        <li>
            <div class="form-edit">
                <a href="#"></a>
                <em onclick="edit(this)" class="material-icons">border_color</em>
                <div>
                    <em onclick="commit(this)" class="material-icons">check</em>
                    <em onclick="discard(this)" class="material-icons">clear</em>
                </div>
            </div>
        </li>
    </ul>
    <button type="submit">submit</button>
    </form:form>
</section>

<c:set var = "addressCount" scope = "page" value = "0"/>
<c:forEach items="${user.addresses}" var="address">
<section class="address-section">
    <c:set var="addressCount" value="${addressCount + 1}" scope="page"/>
    <h2>Address ${addressCount}</h2>
    <form:form action="/profile/address/update" method="post" modelAttribute="userAddressDto">
    <ul>
        <li>
            <form:input type="hidden" path="id"
                        value="${address.id}"
                        required="required"/>
        </li>

        <li>
            <label for="countryId">Country</label>
            <form:select class="countrySelect" path="countryId" required="required"
                         data-currentValue="${address.country.id}" >
                <c:forEach items="${countries}" var="country">
                    <c:choose>
                        <c:when test="${country.id == address.country.id}">
                            <option selected="selected" value="${country.id}">${address.country.name}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${country.id}">${country.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </form:select>
        </li>
        <li>
            <label for="city">City</label>
            <form:input type="text" path="city"
                        data-currentValue="${address.city}"
                        value="${address.city}"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="city" cssClass="error" />
        </li>
        <li>
            <label for="postalCode">Postal code</label>
            <form:input type="text" path="postalCode"
                        data-currentValue="${address.postalCode}"
                        value="${address.postalCode}"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="postalCode" cssClass="error" />
        </li>
        <li>
            <label for="street">Street</label>
            <form:input type="text" path="street"
                        data-currentValue="${address.street}"
                        value="${address.street}"
                        onfocusout="validate(this)"
                        required="required" />
            <form:errors path="street" cssClass="error" />
        </li>
        <li>
            <ul>
                <li>
                    <label for="house">House</label>
                    <form:input type="text" path="house"
                                data-currentValue="${address.house}"
                                value="${address.house}"
                                onfocusout="validate(this)"
                                required="required" />
                    <form:errors path="house" cssClass="error" />
                </li>
                <li>
                    <label for="apartment">Apartment</label>
                    <form:input type="text" path="apartment"
                                data-currentValue="${address.apartment}"
                                value="${address.apartment}"
                                onfocusout="validate(this)"
                                required="required" />
                    <form:errors path="apartment" cssClass="error" />
                </li>
            </ul>
        </li>
        <li>
            <div class="form-edit">
                <c:if test = "${user.addresses.size() > 1}">
                    <a href="#" onclick="removeAddress('${address.id}', '${address.country.name}',
                            '${address.city}', '${address.postalCode}', '${address.street}',
                            '${address.house}', '${address.apartment}')">Remove this address</a>
                </c:if>

                <c:if test = "${user.addresses.size() == 1}">
                    <a href="#"></a>
                </c:if>


                <em onclick="edit(this)" class="material-icons">border_color</em>
                <div>
                    <em onclick="commit(this)" class="material-icons">check</em>
                    <em onclick="discard(this)" class="material-icons">clear</em>
                </div>
            </div>
        </li>
    </ul>
    <button type="submit">Submit</button>
    </form:form>
</section>
</c:forEach>


</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/profile.js"></script>
</body>
</html>
