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
    <title>Create product</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>
<%@ include file="partials/create-product-aside.jsp" %>

<main id="create-product-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <h1 id="category-heading"></h1>
    <h1>Create product</h1>
    <form:form action="/product/create"
               method="post"
               modelAttribute="productDto">
        <ul>
        <li>
            <label for="categoryId">Category</label>
            <form:select path="categoryId" required="required">
                <option selected="selected" disabled="disabled">Choose category</option>
                <c:forEach items="${categoryLeafs}" var="category">
                    <option value="${category.id}">${category.id} ${category.name}</option>
                </c:forEach>
            </form:select>

            <form:errors path="categoryId" cssClass="error" />
        </li>
        <li>
            <label for="name">Product name</label>
            <form:input type="text" path="name"
                        onfocusout="validate(this)"
                        placeholder="Enter product name"
                        required="required" />
            <form:errors path="name" cssClass="error" />
        </li>
        <li>
            <label for="purchasingPrice">Purchasing price</label>
            <form:input type="text" path="purchasingPrice"
                        onfocusout="validate(this)"
                        placeholder="Enter purchasing price"
                        required="required" />
            <form:errors path="purchasingPrice" cssClass="error" />
        </li>
        <li>
            <label for="sellingPrice">Selling price</label>
            <form:input type="text" path="sellingPrice"
                        onfocusout="validate(this)"
                        placeholder="Enter selling price"
                        required="required" />
            <form:errors path="sellingPrice" cssClass="error" />
        </li>
        <li>
            <label for="height">Height</label>
            <form:input type="text" path="height"
                        onfocusout="validate(this)"
                        placeholder="Enter product height in meters"
                        required="required" />
            <form:errors path="height" cssClass="error" />
        </li>
        <li>
            <label for="width">Width</label>
            <form:input type="text" path="width"
                        onfocusout="validate(this)"
                        placeholder="Enter product width in meters"
                        required="required" />
            <form:errors path="width" cssClass="error" />
        </li>
        <li>
            <label for="depth">Depth</label>
            <form:input type="text" path="depth"
                        onfocusout="validate(this)"
                        placeholder="Enter product depth in meters"
                        required="required" />
            <form:errors path="depth" cssClass="error" />
        </li>
        <li>
            <label for="weight">Weight</label>
            <form:input type="text" path="weight"
                        onfocusout="validate(this)"
                        placeholder="Enter product wight in kilograms"
                        required="required" />
            <form:errors path="weight" cssClass="error" />
        </li>
        <li>
            <label for="inStock">Quantity in stock</label>
            <form:input type="text" path="inStock"
                        onfocusout="validate(this)"
                        placeholder="Enter product quantity"
                        required="required" />
            <form:errors path="inStock" cssClass="error" />
        </li>
        <li>
            <label for="measureUnitsId">Measure units</label>
            <form:select path="measureUnitsId" required="required">
                <option selected="selected" disabled="disabled">Choose measure units</option>
                <c:forEach items="${allMeasureUnits}" var="measureUnits">
                    <option value="${measureUnits.id}">${measureUnits.name}</option>
                </c:forEach>
            </form:select>
        </li>
            <form:input type="hidden" path="imageUrl"
                        value="/resources/img/no-image-added.png" />
        </ul>
        <button type="submit">Create product</button>
    </form:form>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/create-product.js"></script>
</body>
</html>