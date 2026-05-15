<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register | Pokhara Bites</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="auth-shell">
    <section class="auth-panel">
        <p class="eyebrow">Join Pokhara Bites</p>
        <h1>Create your account</h1>
        <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/register" class="form-card">
            <label>Name <input type="text" name="name" value="${fn:escapeXml(param.name)}"></label>
            <label>Email <input type="text" name="email" value="${fn:escapeXml(param.email)}"></label>
            <label>Password <input type="password" name="password"></label>
            <label>Role
                <select name="role">
                    <option value="customer">Customer</option>
                    <option value="admin">Admin</option>
                </select>
            </label>
            <button type="submit" class="primary">Register</button>
        </form>
        <p class="muted">Already registered? <a href="${pageContext.request.contextPath}/login">Login</a></p>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
