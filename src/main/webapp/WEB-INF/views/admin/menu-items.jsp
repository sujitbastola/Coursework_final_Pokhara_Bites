<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Menu Items | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Admin</p><h1>Menu Items</h1></div><a class="button primary" href="${pageContext.request.contextPath}/admin/menu-items/add">Add Menu Item</a></div>
    <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
    <c:if test="${not empty param.error}"><div class="alert error"><c:out value="${param.error}"/></div></c:if>
    <section class="panel">
        <table>
            <tr><th>Restaurant</th><th>Name</th><th>Category</th><th>Price</th><th>Available</th><th>Actions</th></tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.restaurantName}"/></td><td><c:out value="${item.name}"/></td>
                    <td><span class="pill"><c:out value="${item.category}"/></span></td>
                    <td>Rs. <fmt:formatNumber value="${item.price}" minFractionDigits="2"/></td>
                    <td>${item.available ? 'Yes' : 'No'}</td>
                    <td class="actions">
                        <a class="button" href="${pageContext.request.contextPath}/admin/menu-items/edit?id=${item.id}">Edit</a>
                        <form method="post" action="${pageContext.request.contextPath}/admin/menu-items/delete">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="danger">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </section>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
