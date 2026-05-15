<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Restaurants | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="hero restaurant-hero">
        <p class="eyebrow">Lakeside flavors, server-side ordering</p>
        <h1>Choose a restaurant in Pokhara</h1>
        <p>Search local kitchens, tick one card, and browse its menu without JavaScript.</p>
    </div>
    <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
    <c:if test="${not empty param.error || not empty error}"><div class="alert error"><c:out value="${empty error ? param.error : error}"/></div></c:if>

    <form method="get" action="${pageContext.request.contextPath}/restaurants" class="search-card search-form">
        <input type="text" name="q" value="${q}" placeholder="Search restaurants, cuisine, or area">
        <button class="primary" type="submit">Search</button>
        <c:if test="${not empty q}"><a class="button secondary" href="${pageContext.request.contextPath}/restaurants">Clear</a></c:if>
    </form>

    <form method="post" action="${pageContext.request.contextPath}/select-restaurant">
        <section class="grid restaurant-grid">
            <c:choose>
                <c:when test="${empty restaurants}">
                    <div class="empty full-row"><h2>No restaurants found.</h2><p class="muted">Try another name, cuisine, or Pokhara area.</p></div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="restaurant" items="${restaurants}">
                        <label class="card selectable restaurant-card">
                            <c:if test="${not empty restaurant.imageUrl}"><img src="${restaurant.imageUrl}" alt="${restaurant.name}"></c:if>
                            <span class="check-row">
                                <input type="checkbox" name="restaurantId" value="${restaurant.id}" ${sessionScope.selectedRestaurantId == restaurant.id ? 'checked' : ''}>
                                <span><strong><c:out value="${restaurant.name}"/></strong><small>Tap card to select</small></span>
                            </span>
                            <span class="pill"><c:out value="${restaurant.cuisineType}"/></span>
                            <span class="muted"><c:out value="${restaurant.address}"/></span>
                            <span class="muted"><c:out value="${restaurant.openingHours}"/> | <c:out value="${restaurant.phone}"/></span>
                            <span class="button secondary full">Choose This Restaurant</span>
                        </label>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </section>
        <div class="sticky-actions"><button class="primary" type="submit">View Selected Menu</button></div>
    </form>
    <c:if test="${not empty sessionScope.cart}">
        <form method="post" action="${pageContext.request.contextPath}/cart" class="small-form">
            <input type="hidden" name="action" value="clear">
            <button type="submit" class="secondary">Clear Cart To Switch Restaurant</button>
        </form>
    </c:if>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
