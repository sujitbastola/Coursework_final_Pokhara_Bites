<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Restaurants | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Admin</p><h1>Restaurants</h1></div><a class="button primary" href="${pageContext.request.contextPath}/admin/restaurants/add">Add Restaurant</a></div>
    <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
    <c:if test="${not empty param.error || not empty error}"><div class="alert error"><c:out value="${empty error ? param.error : error}"/></div></c:if>
    <section class="grid">
        <c:forEach var="restaurant" items="${restaurants}">
            <article class="card">
                <c:if test="${not empty restaurant.imageUrl}"><img src="${restaurant.imageUrl}" alt="${restaurant.name}"></c:if>
                <h2><c:out value="${restaurant.name}"/></h2>
                <p><c:out value="${restaurant.cuisineType}"/></p>
                <p class="muted"><c:out value="${restaurant.address}"/> | <c:out value="${restaurant.phone}"/></p>
                <p class="muted"><c:out value="${restaurant.openingHours}"/></p>
                <div class="actions">
                    <a class="button" href="${pageContext.request.contextPath}/admin/restaurants/edit?id=${restaurant.id}">Edit</a>
                    <form method="post" action="${pageContext.request.contextPath}/admin/restaurants/delete">
                        <input type="hidden" name="id" value="${restaurant.id}">
                        <button type="submit" class="danger">Delete</button>
                    </form>
                </div>
            </article>
        </c:forEach>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>