<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error | Pokhara Bites</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page narrow">
    <section class="empty">
        <p class="eyebrow">Something went wrong</p>
        <h1>
            <c:choose>
                <c:when test="${requestScope['jakarta.servlet.error.status_code'] == 404}">Page Not Found</c:when>
                <c:otherwise>Application Error</c:otherwise>
            </c:choose>
        </h1>
        <p class="muted">
            <c:choose>
                <c:when test="${requestScope['jakarta.servlet.error.status_code'] == 404}">
                    The page you requested does not exist.
                </c:when>
                <c:otherwise>
                    We could not complete that request. Please try again from the main restaurant page.
                </c:otherwise>
            </c:choose>
        </p>
        <div class="actions center-actions">
            <a class="button primary" href="${pageContext.request.contextPath}/restaurants">View Restaurants</a>
            <c:if test="${empty sessionScope.user}">
                <a class="button" href="${pageContext.request.contextPath}/login">Login</a>
            </c:if>
        </div>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
