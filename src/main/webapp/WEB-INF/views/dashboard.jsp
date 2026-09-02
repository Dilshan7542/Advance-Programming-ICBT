<% request.setAttribute("pageTitle", "Dashboard - Sunrise Dental Clinic"); %>
<%@ include file="common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Clinic Dashboard</h1>
        <p class="subtitle">Appointment, patient and billing summary.</p>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/appointments/add">Register New Appointment</a>
</div>

<c:if test="${not empty error}">
    <div class="alert error"><c:out value="${error}"/></div>
</c:if>

<div class="grid grid-4">
    <section class="card stat">
        <div class="number"><c:out value="${stats.totalPatients}" default="0"/></div>
        <div class="label">Registered Patients</div>
    </section>
    <section class="card stat">
        <div class="number"><c:out value="${stats.totalAppointments}" default="0"/></div>
        <div class="label">Total Appointments</div>
    </section>
    <section class="card stat">
        <div class="number"><c:out value="${stats.todayAppointments}" default="0"/></div>
        <div class="label">Today's Appointments</div>
    </section>
    <section class="card stat">
        <div class="number"><c:out value="${stats.scheduledAppointments}" default="0"/></div>
        <div class="label">Scheduled Appointments</div>
    </section>
</div>

<div class="grid grid-2">
    <section class="card">
        <h2>Paid Revenue</h2>
        <div class="amount">LKR <c:out value="${stats.paidRevenue}" default="0.00"/></div>
        <p class="subtitle">Calculated from bills marked as paid.</p>
    </section>
    <section class="card">
        <h2>Quick Appointment Search</h2>
        <form class="search-bar" method="get" action="${pageContext.request.contextPath}/appointments/">
            <input type="text" name="q" placeholder="Appointment number or patient name" required>
            <button class="btn" type="submit">Search</button>
        </form>
    </section>
</div>
<%@ include file="common/footer.jspf" %>
