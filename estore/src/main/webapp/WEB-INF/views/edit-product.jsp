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
<body>
<%@ include file="partials/topnav.jsp" %>

<aside id="edit-product-aside">
    <ul>
        <li>
            <a href="/product/${product.id}/attribute/create">Add attribute</a>

        </li>
        <li>
            <a href="/product/${product.id}/attribute/delete">Remove attribute</a>
        </li>
    </ul>
</aside>

<main id="edit-product-main">
    <c:if test="${not empty message}"><div id="message">${message}</div></c:if>
    <c:if test="${not empty error}"><div id="error">${error}</div></c:if>
    <div id="product-image"><img src="${product.imageUrl}" alt=""></div>
    <div>${product.name}</div>

    <section class="details-section">
        <h2>Product details</h2>
        <form:form action="/product/${product.id}/update" method="post" modelAttribute="productDto">
            <ul>
                <li>
                    <label for="categoryId">Category</label>
                    <form:select path="categoryId" required="required"
                                 data-currentValue="${product.category.id}" >
                        <c:forEach items="${categoryLeafs}" var="category">
                            <c:choose>
                                <c:when test="${category.id == product.category.id}">
                                    <option selected="selected"
                                            value="${category.id}">${category.id} ${category.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${category.id}">${category.id} ${category.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="categoryId" cssclass="error" />
                </li>
                <li>
                    <label for="name">Product name</label>
                    <form:input type="text" path="name"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.name}"
                                value="${product.name}"
                                required="required" />
                    <form:errors path="name" cssclass="error" />
                </li>
                <li>
                    <label for="purchasingPrice">Purchasing price</label>
                    <form:input type="text" path="purchasingPrice"
                                onfocusout="validate(this)"
                                data-currentvalue="${purchasingPrice.price}"
                                value="${purchasingPrice.price}"
                                required="required" />
                    <form:errors path="purchasingPrice" cssClass="error" />
                </li>
                <li>
                    <label for="sellingPrice">Selling price</label>
                    <form:input type="text" path="sellingPrice"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.sellingPrice}"
                                value="${product.sellingPrice}"
                                required="required" />
                    <form:errors path="sellingPrice" cssClass="error" />
                </li>
                <li>
                    <label for="height">Product height, meters</label>
                    <form:input type="text" path="height"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.height}"
                                value="${product.height}"
                                placeholder="Enter product height"
                                required="required" />
                    <form:errors path="height" cssClass="error" />
                </li>
                <li>
                    <label for="width">Product width, meters</label>
                    <form:input type="text" path="width"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.width}"
                                value="${product.width}"
                                placeholder="Enter product width"
                                required="required" />
                    <form:errors path="width" cssClass="error" />
                </li>
                <li>
                    <label for="depth">Product depth, meters</label>
                    <form:input type="text" path="depth"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.depth}"
                                value="${product.depth}"
                                placeholder="Enter product depth"
                                required="required" />
                    <form:errors path="depth" cssClass="error" />
                </li>
                <li>
                    <label for="weight">Product weight, kilograms</label>
                    <form:input type="text" path="weight"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.weight}"
                                value="${product.weight}"
                                placeholder="Enter product wight"
                                required="required" />
                    <form:errors path="weight" cssClass="error" />
                </li>
                <li>
                    <label for="inStock">Quantity in stock</label>
                    <form:input type="text" path="inStock"
                                onfocusout="validate(this)"
                                data-currentvalue="${product.quantityInStock}"
                                value="${product.quantityInStock}"
                                placeholder="Enter product quantity"
                                required="required" />
                    <form:errors path="inStock" cssClass="error" />
                </li>
                <li>
                    <label for="measureUnitsId">Measure units</label>
                    <form:select path="measureUnitsId" required="required"
                                data-currentValue="${product.measureUnits.id}">
                        <c:forEach items="${allMeasureUnits}" var="measureUnits">
                            <c:choose>
                                <c:when test="${measureUnits.id == product.measureUnits.id}">
                                    <option selected="selected" value="${measureUnits.id}">${measureUnits.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${measureUnits.id}">${measureUnits.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="measureUnitsId" cssClass="error" />
                </li>
                <li>
                    <div class="form-edit">
                        <a href="/product/${product.id}/image/update">Update image</a>
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

    <section id="product-attributes-section">
        <h2>Product attributes</h2>
        <form action="/product/${product.id}/attributes/update" method="post">
            <ul>
                <c:forEach items="${attributes}" var="attribute">
                    <li>
                        <label>${attribute.pk.attribute.name}</label>
                        <input type="text" name="${attribute.pk.attribute.name}"
                                            value="${attribute.value}" />
                    </li>
                </c:forEach>
                <li>
                    <div class="form-edit">
                        <a href=""></a>
                        <em onclick="edit(this)" class="material-icons">border_color</em>
                        <div>
                            <em onclick="commit(this)" class="material-icons">check</em>
                            <em onclick="discard(this)" class="material-icons">clear</em>
                        </div>
                    </div>
                </li>
            </ul>
            <button type="submit">submit</button>
        </form>
    </section>

</main>

<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/edit-product.js"></script>
</body>
</html>
