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
    <title>Orders</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>

<main id="orders-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1>Orders</h1>
    <table id="orders-table">

        <!-- Header -->
        <tr>
            <th>Ref.</th>
            <th>Date</th>
            <th>Email</th>
            <th>Total price</th>
            <th>Status</th>
            <th></th>
        </tr>

        <!-- Search bar -->
        <form:form action="/orders/find" method="post" modelAttribute="searchOrdersDto" >
        <tr>
            <td>
                <form:input type="text" path="orderId" onfocusout="validate(this)" />
                <form:errors path="orderId" cssClass="error" />

            </td>
            <td>
                <div id="dates">
                    <div>
                        <label for="startDate">From</label>
                        <form:input type="date" path="startDate" onfocusout="validate(this)" />
                        <form:errors path="startDate" cssClass="error" />
                    </div>
                    <div>
                        <label for="endDate">To</label>
                        <form:input type="date" path="endDate" onfocusout="validate(this)" />
                        <form:errors path="endDate" cssClass="error" />
                    </div>
                </div>
            </td>
            <td>
                <form:input type="text" path="userEmail" onfocusout="validate(this)" />
            </td>
            <td>
                <div id="prices">
                    <div>
                        <label for="minPrice">From</label>
                        <form:input type="text" path="minPrice" onfocusout="validate(this)" />
                        <form:errors path="minPrice" cssClass="error" />
                    </div>
                    <div>
                        <label for="maxPrice">To</label>
                        <form:input type="text" path="maxPrice" onfocusout="validate(this)"/>
                        <form:errors path="maxPrice" cssClass="error" />
                    </div>
                </div>
            </td>
            <td>
                <form:select path="orderStatus">
                    <c:if test="${searchOrdersDto.orderStatus != null}">
                        <option selected="selected"
                                value="${searchOrdersDto.orderStatus}">${searchOrdersDto.orderStatus.value}</option>
                    </c:if>
                    <c:if test="${searchOrdersDto.orderStatus == null}">
                        <option selected="selected"
                                value="${null}">All</option>
                    </c:if>

                    <c:forEach items="${orderStatuses}" var="orderStatus">
                        <c:if test="${orderStatus != searchOrdersDto.orderStatus}">
                            <option value="${orderStatus}">${orderStatus.value}</option>
                        </c:if>
                    </c:forEach>
                    <c:if test="${searchOrdersDto.orderStatus != null}">
                        <option value="${null}">All</option>
                    </c:if>
                </form:select>
            </td>
            <td>
                <form:input type="hidden" path="sortBy" />
                <form:input type="hidden" path="sortDirection" />
                <form:button id="findButton">Find</form:button>
            </td>
        </tr>
        </form:form>

        <!-- Sorting bar -->
        <tr>
            <td>
                <div>
                    <em onclick="sortBy('ID', 'ASC')" class="material-icons">keyboard_arrow_up</em>
                    <em onclick="sortBy('ID', 'DESC')" class="material-icons">keyboard_arrow_down</em>
                </div>
            </td>
            <td>
                <div>
                    <em onclick="sortBy('DATE', 'ASC')" class="material-icons">keyboard_arrow_up</em>
                    <em onclick="sortBy('DATE', 'DESC')" class="material-icons">keyboard_arrow_down</em>
                </div>
            </td>
            <td>
                <div>
                    <em onclick="sortBy('EMAIL', 'ASC')" class="material-icons">keyboard_arrow_up</em>
                    <em onclick="sortBy('EMAIL', 'DESC')" class="material-icons">keyboard_arrow_down</em>
                </div>
            </td>
            <td>
                <div>
                    <em onclick="sortBy('PRICE', 'ASC')" class="material-icons">keyboard_arrow_up</em>
                    <em onclick="sortBy('PRICE', 'DESC')" class="material-icons">keyboard_arrow_down</em>
                </div>
            </td>
            <td></td>
            <td></td>
        </tr>

        <!-- Table content -->
        <c:forEach items="${orders}" var="order">
            <tr class="order-row">
                <td>${order.id}</td>
                <td>${order.creationDateTime.toLocalDate()}</td>
                <td>${order.user.email}</td>
                <td>$${order.totalSellingPrice}</td>
                <td>
                    <select data-currentValue="${order.orderStatus.value}">
                        <c:forEach items="${orderStatuses}" var="orderStatus">
                            <c:choose>
                                <c:when test="${orderStatus == order.orderStatus}">
                                    <option selected="selected"
                                            value="${orderStatus}">${orderStatus.value}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${orderStatus}">${orderStatus.value}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <button onclick="toggleOrderDetails(this, ${order.id}, ${order.totalSellingPrice}, false)">
                        <em class="material-icons">keyboard_arrow_down</em>
                    </button>
                </td>
            </tr>
            <tr class="order-details-row" style="display: none;">
                <td colspan="6">
                    <table class="dropdown-table">
                        <tr>
                            <th></th>
                            <th></th>
                            <th>Price</th>
                            <th>Qty</th>
                            <th>Amount</th>
                        </tr>
                        <tr>
                            <th>
                                <div class="form-edit">
                                    <em onclick="edit(this)" class="material-icons">border_color</em>
                                    <div>
                                        <em onclick="commit(this, ${order.id})" class="material-icons">check</em>
                                        <em onclick="discard(this)" class="material-icons">clear</em>
                                    </div>
                                </div>
                            </th>
                            <th></th>
                            <th></th>
                            <th>Total:</th>
                            <th>$${order.totalSellingPrice}</th>
                        </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/orders.js"></script>
</body>
</html>
