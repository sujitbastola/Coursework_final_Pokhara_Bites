<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Admin Dashboard | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Admin</p><h1>Dashboard</h1></div></div>
    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>
    <section class="stats-grid">
        <div class="stat"><span>Users</span><strong>${userCount}</strong></div>
        <div class="stat"><span>Restaurants</span><strong>${restaurantCount}</strong></div>
        <div class="stat"><span>Menu Items</span><strong>${menuCount}</strong></div>
        <div class="stat"><span>Orders</span><strong>${orderCount}</strong></div>
    </section>
    <section class="panel">
        <h2>Recent Orders</h2>
        <table>
            <tr><th>ID</th><th>Restaurant</th><th>Total</th><th>Status</th><th>Date</th></tr>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>#${order.id}</td><td><c:out value="${order.restaurantName}"/></td>
                    <td>Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></td>
                    <td><span class="pill"><c:out value="${order.status}"/></span></td><td>${order.orderDate}</td>
                </tr>
            </c:forEach>
        </table>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
