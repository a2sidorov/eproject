<%@ include file="partials/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <%@ include file="partials/head.jsp" %>
</head>

<body onload="getProducts()">
<%@ include file="partials/topnav.jsp" %>

<div id="home-categories">
    <h3>SHOP BY CATEGORY</h3>
    <ul>
        <c:set var="categories" value="${categories}" scope="request"/>
        <jsp:include page="partials/home-category.jsp"/>
    </ul>
</div>

<main id="home-main" >
    <h1 id="categoryName"></h1>
    <div id="search-bar">
        <form action="">
            <input id="searchInput" type="text">
            <input id="categoryIdInput" type="hidden" value="">
            <button type="button" onclick="findProducts()"><em class="material-icons">search</em></button>
        </form>
    </div>
    <section id="filter-section">
        <h2>Filter by attributes</h2>
        <ul id="attribute-list">
        </ul>
        <div id="use-filter-div">
            <label>Apply filter</label>
            <input onclick="applyFilter(this)" type="checkbox">
        </div>
    </section>

    <ul id="product-list">
    </ul>

    <ul id="product-list-navbar">
    </ul>

</main>
<%@ include file="partials/footer.jsp" %>

<script src="/resources/js/home.js"></script>
</body>
</html>
