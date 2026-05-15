<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head><title>Edit Menu Item | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page narrow">
    <p class="eyebrow">Admin</p><h1>Edit Menu Item</h1>
    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/admin/menu-items/edit" class="form-card">
        <input type="hidden" name="id" value="${item.id}">
        <label>Restaurant
            <select name="restaurantId">
                <c:forEach var="restaurant" items="${restaurants}">
                    <option value="${restaurant.id}" ${item.restaurantId == restaurant.id ? 'selected' : ''}><c:out value="${restaurant.name}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Name <input type="text" name="name" value="${fn:escapeXml(item.name)}"></label>
        <label>Description <textarea name="description"><c:out value="${item.description}"/></textarea></label>
        <label>Price <input type="text" name="price" value="${fn:escapeXml(item.price)}"></label>
        <label>Category
            <select name="category">
                <option value="veg" ${item.category == 'veg' ? 'selected' : ''}>Veg</option>
                <option value="non-veg" ${item.category == 'non-veg' ? 'selected' : ''}>Non-veg</option>
            </select>
        </label>
        <label>Availability
            <select name="availability">
                <option value="true" ${item.available ? 'selected' : ''}>Yes</option>
                <option value="false" ${not item.available ? 'selected' : ''}>No</option>
            </select>
        </label>
        <button class="primary" type="submit">Update Menu Item</button>
    </form>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
