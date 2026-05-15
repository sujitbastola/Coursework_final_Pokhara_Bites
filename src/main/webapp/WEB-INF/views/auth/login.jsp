<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login | Pokhara Bites</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="auth-shell">
    <section class="auth-panel">
        <p class="eyebrow">Welcome back</p>
        <h1>Login to Pokhara Bites</h1>
        <c:if test="${not empty error || not empty param.error}"><div class="alert error"><c:out value="${empty error ? param.error : error}"/></div></c:if>
        <c:if test="${not empty param.success}"><div class="alert success"><c:out value="${param.success}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" class="form-card">
            <label>Email <input type="text" name="email" value="${fn:escapeXml(param.email)}"></label>
            <label>Password <input type="password" name="password"></label>
            <button type="submit" class="primary">Login</button>
        </form>
        <p class="muted">New here? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
