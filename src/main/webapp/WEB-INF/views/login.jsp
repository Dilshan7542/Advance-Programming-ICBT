<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="login-page">
<section class="card login-card">
    <div class="login-logo">&#129463;</div>
    <h1 class="login-title">Sunrise Dental Clinic</h1>
    <p class="login-note">Authorized staff access only</p>

    <c:if test="${param.logout == '1'}">
        <div class="alert success">You have safely exited the system.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label class="required" for="username">Username</label>
            <input id="username" name="username" type="text" value="<c:out value='${username}'/>" required autofocus>
        </div>
        <div class="form-group" style="margin-top: 14px;">
            <label class="required" for="password">Password</label>
            <input id="password" name="password" type="password" required>
        </div>
        <button class="btn" type="submit" style="width: 100%; margin-top: 20px;">Login</button>
    </form>
    <p class="login-note">Default demonstration account: admin / admin123</p>
</section>
</body>
</html>
