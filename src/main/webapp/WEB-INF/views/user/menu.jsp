<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Menu | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Menu</p><h1><c:out value="${restaurant.name}"/></h1><p><c:out value="${restaurant.cuisineType}"/> | <c:out value="${restaurant.openingHours}"/></p></div><a class="button" href="${pageContext.request.contextPath}/restaurants">Change Restaurant</a></div>
    <c:if test="${not empty param.error}"><div class="alert error"><c:out value="${param.error}"/></div></c:if>

    <form method="get" action="${pageContext.request.contextPath}/menu" class="search-card search-form menu-toolbar">
        <input type="text" name="q" value="${q}" placeholder="Search momo, thakali, chiya, lassi">
        <button class="primary" type="submit">Search</button>
        <c:if test="${not empty q}"><a class="button secondary" href="${pageContext.request.contextPath}/menu">Clear</a></c:if>
    </form>

    <section class="grid menu-grid">
        <c:choose>
            <c:when test="${empty items}">
                <div class="empty full-row"><h2>No menu items found.</h2><p class="muted">Try another Nepali food or drink name.</p></div>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${items}">
                    <article class="card menu-card">
                        <div><span class="pill"><c:out value="${item.category}"/></span><h2><c:out value="${item.name}"/></h2></div>
                        <p><c:out value="${item.description}"/></p>
                        <a class="button secondary" href="#item-${item.id}">Details</a>
                        <div id="item-${item.id}" class="popup">
                            <div class="popup-box">
                                <a class="popup-close" href="#">Close</a>
                                <span class="pill"><c:out value="${item.category}"/></span>
                                <h2><c:out value="${item.name}"/></h2>
                                <p><c:out value="${item.description}"/></p>
                                <strong>Rs. <fmt:formatNumber value="${item.price}" minFractionDigits="2"/></strong>
                            </div>
                        </div>
                        <strong>Rs. <fmt:formatNumber value="${item.price}" minFractionDigits="2"/></strong>
                        <c:choose>
                            <c:when test="${item.available}">
                                <form method="post" action="${pageContext.request.contextPath}/add-to-cart" class="cart-form">
                                    <input type="hidden" name="itemId" value="${item.id}">
                                    <label>Qty <input type="text" name="quantity" value="1"></label>
                                    <button class="primary" type="submit">Add To Cart</button>
                                </form>
                            </c:when>
                            <c:otherwise><span class="unavailable">Unavailable</span></c:otherwise>
                        </c:choose>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
