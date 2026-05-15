<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head><title>Add Restaurant | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page narrow">
    <p class="eyebrow">Admin</p><h1>Add Restaurant</h1>
    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/admin/restaurants/add" class="form-card">
        <label>Name <input type="text" name="name" value="${fn:escapeXml(restaurant.name)}"></label>
        <label>Address <input type="text" name="address" value="${fn:escapeXml(restaurant.address)}"></label>
        <label>Phone <input type="text" name="phone" value="${fn:escapeXml(restaurant.phone)}"></label>
        <label>Cuisine Type <input type="text" name="cuisineType" value="${fn:escapeXml(restaurant.cuisineType)}"></label>
        <label>Opening Hours <input type="text" name="openingHours" value="${fn:escapeXml(restaurant.openingHours)}"></label>
        <label>Image URL <input type="text" name="imageUrl" value="${fn:escapeXml(restaurant.imageUrl)}"></label>
        <button class="primary" type="submit">Save Restaurant</button>
    </form>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
