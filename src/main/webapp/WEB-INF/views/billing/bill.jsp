<% request.setAttribute("pageTitle", "Patient Bill - Sunrise Dental Clinic"); %>
<%@ include file="../common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Patient Bill</h1>
        <p class="subtitle">Treatment cost plus consultation fee.</p>
    </div>
    <a class="btn secondary" href="${pageContext.request.contextPath}/appointments/">Appointments</a>
</div>

<c:if test="${param.updated == '1'}">
    <div class="alert success">Bill payment status updated.</div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert error"><c:out value="${error}"/></div>
</c:if>

<c:if test="${not empty bill}">
<section class="card">
    <div class="page-header">
        <div>
            <h2><c:out value="${bill.appointmentNo}"/></h2>
            <span class="badge ${bill.status}"><c:out value="${bill.status}"/></span>
        </div>
        <a class="btn" target="_blank" href="${pageContext.request.contextPath}/billing/print?id=${bill.appointmentId}">Print PDF Receipt</a>
    </div>

    <div class="detail-grid">
        <div class="key">Patient</div><div><c:out value="${bill.patientName}"/> (<c:out value="${bill.contactNumber}"/>)</div>
        <div class="key">Dentist</div><div><c:out value="${bill.dentistName}"/></div>
        <div class="key">Treatment</div><div><c:out value="${bill.treatmentName}"/></div>
        <div class="key">Appointment</div><div><c:out value="${bill.appointmentDate}"/> at <c:out value="${bill.appointmentTime}"/></div>
        <div class="key">Consultation Fee</div><div>LKR <c:out value="${bill.consultationFee}"/></div>
        <div class="key">Treatment Fee</div><div>LKR <c:out value="${bill.treatmentFee}"/></div>
        <div class="key">Total Amount</div><div class="amount">LKR <c:out value="${bill.totalAmount}"/></div>
        <div class="key">Paid At</div><div><c:out value="${bill.paidAt}" default="-"/></div>
    </div>

    <div class="actions">
        <c:if test="${bill.status == 'UNPAID'}">
            <form method="post" action="${pageContext.request.contextPath}/billing">
                <input type="hidden" name="id" value="${bill.appointmentId}">
                <input type="hidden" name="status" value="PAID">
                <button class="btn success" type="submit">Mark as Paid</button>
            </form>
        </c:if>
        <c:if test="${bill.status == 'PAID'}">
            <form method="post" action="${pageContext.request.contextPath}/billing">
                <input type="hidden" name="id" value="${bill.appointmentId}">
                <input type="hidden" name="status" value="UNPAID">
                <button class="btn danger" type="submit">Mark as Unpaid</button>
            </form>
        </c:if>
        <a class="btn secondary" href="${pageContext.request.contextPath}/appointments/view?id=${bill.appointmentId}">View Appointment</a>
    </div>
</section>
</c:if>
<%@ include file="../common/footer.jspf" %>
