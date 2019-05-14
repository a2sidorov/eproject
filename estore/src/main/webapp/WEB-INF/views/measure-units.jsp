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
  ~  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  ~ SOFTWARE.
  ~
  --%>
<%@ include file="partials/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product measure units</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>
<%@ include file="partials/create-product-aside.jsp" %>

<main id="measure-units-main">
    <div id="error"></div>
    <h1>Product measure units</h1>
    <ul>
        <c:forEach items="${measureUnitsList}" var="measureUnits">
            <li>
                <span>${measureUnits.name}</span>
                <button onclick="deleteMeasureUnits(${measureUnits.id})">
                    <em class="material-icons">clear</em>
                </button>
                <button onclick="renameMeasureUnits(${measureUnits.id})">
                    <em class="material-icons">border_color</em>
                </button>
            </li>
        </c:forEach>
        <li>
            <button onclick="createMeasureUnits()">Create measure units</button>
        </li>
    </ul>

</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/measure-units.js"></script>
</body>
</html>
