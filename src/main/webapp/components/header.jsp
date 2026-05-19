<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <a class="brand" href="${pageContext.request.contextPath}/index.jsp">
        <span class="brand-mark">PB</span>
        <span>Pokhara Bites</span>
    </a>
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/about">About Us</a>
        <a href="${pageContext.request.contextPath}/restaurants">Restaurants</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/admin/restaurants">Restaurants</a>
                    <a href="${pageContext.request.contextPath}/admin/menu-items">Menu</a>
                </c:if>
                <c:if test="${not sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/menu">Menu</a>
                    <a href="${pageContext.request.contextPath}/orders">Orders</a>
                    <a href="${pageContext.request.contextPath}/about">Orders</a>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/logout" class="inline-form">
                    <button class="link-button" type="submit">Logout</button>
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
