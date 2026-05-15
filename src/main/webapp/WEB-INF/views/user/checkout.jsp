<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Checkout | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page narrow">
    <p class="eyebrow">Checkout</p><h1>Confirm Your Order</h1>
    <c:if test="${not empty param.error}"><div class="alert error"><c:out value="${param.error}"/></div></c:if>
    <section class="panel">
        <c:forEach var="cartItem" items="${sessionScope.cart}">
            <div class="line-item"><span><c:out value="${cartItem.menuItem.name}"/> x ${cartItem.quantity}</span><strong>Rs. <fmt:formatNumber value="${cartItem.subtotal}" minFractionDigits="2"/></strong></div>
        </c:forEach>
        <div class="total-line">Total <strong>Rs. <fmt:formatNumber value="${cartTotal}" minFractionDigits="2"/></strong></div>
        <form method="post" action="${pageContext.request.contextPath}/place-order">
            <button class="primary full" type="submit">Place Order</button>
        </form>
    </section>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
