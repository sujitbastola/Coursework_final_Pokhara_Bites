<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.restaurant.dao.RestaurantDAO" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    try {
        request.setAttribute("restaurants", new RestaurantDAO().findAll());
    } catch (Exception e) {
        request.setAttribute("error", "Unable to load restaurants.");
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pokhara Bites | Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page">
    <section class="hero simple-hero">
        <p class="eyebrow">Pokhara Bites</p>
        <h1>Restaurant Management System</h1>
        <p>Choose a restaurant, view its menu, add food to cart, and place an order using simple JSP and Servlet pages.</p>
        <div class="actions">
            <a class="button primary" href="${pageContext.request.contextPath}/restaurants">Browse Restaurants</a>
            <a class="button secondary" href="${pageContext.request.contextPath}/login">Login</a>
        </div>
    </section>

    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>

    <section class="page-title compact">
        <div>
            <p class="eyebrow">Available restaurants</p>
            <h1>Order from local kitchens</h1>
        </div>
    </section>

    <section class="grid">
        <c:forEach var="restaurant" items="${restaurants}">
            <article class="card">
                <h2><c:out value="${restaurant.name}"/></h2>
                <p><c:out value="${restaurant.cuisineType}"/></p>
                <p class="muted"><c:out value="${restaurant.address}"/></p>
                <p class="muted"><c:out value="${restaurant.openingHours}"/> | <c:out value="${restaurant.phone}"/></p>
                <form method="post" action="${pageContext.request.contextPath}/select-restaurant" class="small-form">
                    <input type="hidden" name="restaurantId" value="${restaurant.id}">
                    <button class="primary full" type="submit">View Menu</button>
                </form>
            </article>
        </c:forEach>
        <c:if test="${empty restaurants}">
            <div class="empty">No restaurants found. Please create restaurants from the admin dashboard.</div>
        </c:if>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
