<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><title>Cart | Pokhara Bites</title><link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"></head>
<body><jsp:include page="/components/header.jsp"/>
<main class="page">
    <div class="page-title"><div><p class="eyebrow">Cart</p><h1>Your Order Basket</h1></div><a class="button" href="${pageContext.request.contextPath}/menu">Back To Menu</a></div>
    <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
    <c:if test="${not empty param.error}"><div class="alert error"><c:out value="${param.error}"/></div></c:if>
    <c:choose>
        <c:when test="${empty sessionScope.cart}">
            <section class="empty"><h2>Your cart is empty.</h2><a class="button primary" href="${pageContext.request.contextPath}/restaurants">Choose Restaurant</a></section>
        </c:when>
        <c:otherwise>
            <section class="panel">
                <table>
                    <tr><th>Item</th><th>Price</th><th>Qty</th><th>Subtotal</th><th></th></tr>
                    <c:forEach var="cartItem" items="${sessionScope.cart}">
                        <tr>
                            <td><c:out value="${cartItem.menuItem.name}"/></td>
                            <td>Rs. <fmt:formatNumber value="${cartItem.menuItem.price}" minFractionDigits="2"/></td>
                            <td>${cartItem.quantity}</td>
                            <td>Rs. <fmt:formatNumber value="${cartItem.subtotal}" minFractionDigits="2"/></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="itemId" value="${cartItem.menuItem.id}">
                                    <button type="submit" class="secondary">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="checkout-bar">
                    <strong>Total: Rs. <fmt:formatNumber value="${cartTotal}" minFractionDigits="2"/></strong>
                    <form method="post" action="${pageContext.request.contextPath}/cart"><input type="hidden" name="action" value="clear"><button class="secondary" type="submit">Clear Cart</button></form>
                    <a class="button primary" href="${pageContext.request.contextPath}/checkout">Checkout</a>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main><jsp:include page="/components/footer.jsp"/></body>
</html>
