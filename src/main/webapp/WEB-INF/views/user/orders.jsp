<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Orders | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Orders</p><h1>Your Past Orders</h1></div><a class="button primary" href="${pageContext.request.contextPath}/restaurants">Order Again</a></div>
    <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
    <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>
    <section class="order-list">
        <c:forEach var="order" items="${orders}">
            <article class="panel">
                <div class="page-title compact"><div><h2>Order #${order.id}</h2><p><c:out value="${order.restaurantName}"/> | ${order.orderDate}</p></div><span class="pill"><c:out value="${order.status}"/></span></div>
                <c:forEach var="item" items="${order.items}">
                    <div class="line-item"><span><c:out value="${item.itemName}"/> x ${item.quantity}</span><span>Rs. <fmt:formatNumber value="${item.price}" minFractionDigits="2"/></span></div>
                </c:forEach>
                <div class="total-line">Total <strong>Rs. <fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></strong></div>
            </article>
        </c:forEach>
        <c:if test="${empty orders}"><section class="empty"><h2>No orders yet.</h2></section></c:if>
    </section>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
