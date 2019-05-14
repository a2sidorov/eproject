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
    <title>Revenue</title>
    <%@ include file="partials/head.jsp" %>
</head>
<body>
<%@ include file="partials/topnav.jsp" %>
<%@ include file="partials/reports-aside.jsp" %>


<main id="revenue-main">
    <div id="error" class="error"></div>

    <h1>Revenue</h1>
    <form action="#" method="post">
        <ul>
            <li>
                <label>From</label>
                <input id="start" type="date" required="required"/>
            </li>
            <li>
                <label>To</label>
                <input id="end" type="date" required="required"/>
            </li>
            <li>
                <ul id="interval-radiobuttons">
                    <li>
                        <input type="radio" name="interval" value="monthly" onclick="chooseIntervalMonthly()"
                        checked>
                        <span>Monthly</span>
                    </li>
                    <li>
                        <input type="radio" name="interval" value="weekly" onclick="chooseIntervalWeekly()">
                        <span>Weekly</span>
                    </li>
                </ul>

            </li>
            <li>
                <button type="button" onclick="getRevenues()">Show</button>
            </li>
        </ul>
        <button id="submit-button" type="submit"></button>
    </form>

    <table id="revenue-table">
    </table>
</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/revenue.js"></script>
</body>
</html>
