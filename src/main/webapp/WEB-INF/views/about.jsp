<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>About Us | Pokhara Bites</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<jsp:include page="/components/header.jsp"/>
<main class="page">
    <section class="hero about-hero">
        <p class="eyebrow">About us</p>
        <h1>Six people building simpler local food ordering</h1>
        <p>Pokhara Bites is a restaurant management system focused on quick menu discovery, easy checkout and fast delivery.</p>
    </section>

    <section class="page-title compact">
        <div>
            <p class="eyebrow">Our team</p>
            <h1>Meet the people behind Pokhara Bites</h1>
        </div>
    </section>

    <section class="grid team-grid">
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/sujit-basola.svg" alt="Sujit Basola">
            <h2>SUJIT BASOLA</h2>
            <p class="muted">Project Lead</p>
            <p>Coordinates the restaurant workflow and keeps the ordering experience simple from search to checkout.</p>
        </article>
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/sonish-poudel.svg" alt="Sonish Poudel">
            <h2>SONISH POUDEL</h2>
            <p class="muted">Frontend Designer</p>
            <p>Shapes the JSP pages, responsive layout, and food browsing screens for customers.</p>
        </article>
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/gaurab-poudel.svg" alt="Gaurab Poudel">
            <h2>GAURAB POUDEL</h2>
            <p class="muted">Backend Developer</p>
            <p>Builds the servlet routes, cart behavior, checkout flow, and secure login handling.</p>
        </article>
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/minham-shig-ale.svg" alt="Minham Shig Ale">
            <h2>MINHAM SHIG ALE</h2>
            <p class="muted">Database Developer</p>
            <p>Maintains the MySQL schema, restaurant records, menu items, and order data.</p>
        </article>
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/prajit-laude.svg" alt="Prajit Laude">
            <h2>PRAJIT KC</h2>
            <p class="muted">QA Tester</p>
            <p>Tests customer flows, admin actions, form validation, and edge cases before release.</p>
        </article>
        <article class="card team-card">
            <img class="profile-photo" src="${pageContext.request.contextPath}/images/team/prajwol-laude.svg" alt="Prajwol Laude">
            <h2>PRAJWOL SHRESTHA</h2>
            <p class="muted">Documentation</p>
            <p>Documents setup steps, user roles, project features, and future improvements.</p>
        </article>
    </section>
</main>
<jsp:include page="/components/footer.jsp"/>
</body>
</html>
