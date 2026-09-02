<% request.setAttribute("pageTitle", "Appointment Details - Sunrise Dental Clinic"); %>
<%@ include file="../common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Appointment Details</h1>
        <p class="subtitle">Complete patient and appointment information.</p>
    </div>
    <a class="btn secondary" href="${pageContext.request.contextPath}/appointments/">Back to Appointments</a>
</div>

<c:if test="${param.created == '1'}">
    <div class="alert success">Appointment registered successfully. Keep the appointment number for future searches.</div>
</c:if>
<c:if test="${param.updated == '1'}">
    <div class="alert success">Appointment status updated successfully.</div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert error"><c:out value="${error}"/></div>
</c:if>

<c:if test="${not empty appointment}">
<section class="card">
    <div class="page-header">
        <div>
            <h2><c:out value="${appointment.appointmentNo}"/></h2>
            <span class="badge ${appointment.status}"><c:out value="${appointment.status}"/></span>
        </div>
        <c:if test="${appointment.status != 'CANCELLED'}">
            <a class="btn" href="${pageContext.request.contextPath}/billing?id=${appointment.appointmentId}">Calculate / Print Bill</a>
        </c:if>
    </div>

    <div class="detail-grid">
        <div class="key">Patient Name</div><div><c:out value="${appointment.patientName}"/></div>
        <div class="key">Address</div><div><c:out value="${appointment.patientAddress}"/></div>
        <div class="key">Contact Number</div><div><c:out value="${appointment.contactNumber}"/></div>
        <div class="key">Email</div><div><c:out value="${appointment.patientEmail}" default="-"/></div>
        <div class="key">Dentist</div><div><c:out value="${appointment.dentistName}"/> - <c:out value="${appointment.dentistSpecialty}"/></div>
        <div class="key">Treatment Type</div><div><c:out value="${appointment.treatmentName}"/></div>
        <div class="key">Appointment Date</div><div><c:out value="${appointment.appointmentDate}"/></div>
        <div class="key">Appointment Time</div><div><c:out value="${appointment.appointmentTime}"/></div>
        <div class="key">Consultation Fee</div><div>LKR <c:out value="${appointment.consultationFee}"/></div>
        <div class="key">Treatment Fee</div><div>LKR <c:out value="${appointment.treatmentFee}"/></div>
        <div class="key">Bill Status</div><div><span class="badge ${appointment.billStatus}"><c:out value="${appointment.billStatus}" default="NOT GENERATED"/></span></div>
        <div class="key">Notes</div><div><c:out value="${appointment.notes}" default="-"/></div>
    </div>

    <c:if test="${appointment.status == 'SCHEDULED'}">
        <div class="actions">
            <form method="post" action="${pageContext.request.contextPath}/appointments/status">
                <input type="hidden" name="id" value="${appointment.appointmentId}">
                <input type="hidden" name="status" value="COMPLETED">
                <button class="btn success" type="submit">Mark Completed</button>
            </form>
            <form method="post" action="${pageContext.request.contextPath}/appointments/status" onsubmit="return confirm('Cancel this appointment?');">
                <input type="hidden" name="id" value="${appointment.appointmentId}">
                <input type="hidden" name="status" value="CANCELLED">
                <button class="btn danger" type="submit">Cancel Appointment</button>
            </form>
        </div>
    </c:if>
</section>
</c:if>
<%@ include file="../common/footer.jspf" %>
