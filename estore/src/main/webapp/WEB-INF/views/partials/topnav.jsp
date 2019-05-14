<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<nav id="topnav">
    <a class="material-icons" onclick="openNav()">menu</a>
    <a href="/">Home</a>

    <sec:authorize access="isAuthenticated()">
        <sec:authorize access="hasAuthority('ROLE_CLIENT')">
            <a class="topnav-links" href="/my-orders">My orders</a>
        </sec:authorize>
        <sec:authorize access="hasAuthority('ROLE_MANAGER')">
            <a class="topnav-links" href="/orders">Orders</a>
            <a class="topnav-links" href="/reports/top-products">Reports</a>
            <a class="topnav-links" href="/product/create">Create product</a>
            <a class="topnav-links" href="/categories/edit">Edit categories</a>
        </sec:authorize>
        <sec:authorize access="hasAuthority('ROLE_ADMIN')">
            <a class="topnav-links" href="/users">Users</a>
        </sec:authorize>
    </sec:authorize>

    <sec:authorize access="!isAuthenticated()">
        <a class="topnav-links" href="/signin" sec:authorize access="!isAuthenticated()">Sign in</a>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
        <a class="topnav-links" href="/profile">Profile</a>
        <a class="topnav-links" href="#" onclick="document.getElementById('logoutForm').submit();">Sign out</a>
    </sec:authorize>

    <form id="logoutForm" action="/signout" method="post" hidden="true">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

    <a href="/order">Cart (<span id="productsInCart">${order.calculateTotalNumberOfProducts()}</span>)</a>
</nav>