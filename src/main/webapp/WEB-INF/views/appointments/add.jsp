<% request.setAttribute("pageTitle", "New Appointment - Sunrise Dental Clinic"); %>
<%@ include file="../common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Register New Appointment</h1>
        <p class="subtitle">All fields marked with an asterisk are mandatory.</p>
    </div>
</div>

<c:if test="${not empty error}">
    <div class="alert error"><c:out value="${error}"/></div>
</c:if>

<form class="card" method="post" action="${pageContext.request.contextPath}/appointments/add">
    <h2>Patient Information</h2>
    <div class="form-grid">
        <div class="form-group">
            <label class="required" for="patientName">Patient Name</label>
            <input id="patientName" name="patientName" type="text" maxlength="100" value="<c:out value='${form.patientName}'/>" required>
        </div>
        <div class="form-group">
            <label class="required" for="contactNumber">Contact Number</label>
            <input id="contactNumber" name="contactNumber" type="tel" maxlength="20" value="<c:out value='${form.contactNumber}'/>" required>
        </div>
        <div class="form-group full">
            <label class="required" for="address">Address</label>
            <input id="address" name="address" type="text" maxlength="255" value="<c:out value='${form.address}'/>" required>
        </div>
        <div class="form-group full">
            <label for="email">Email Address</label>
            <input id="email" name="email" type="email" maxlength="100" value="<c:out value='${form.email}'/>">
        </div>
    </div>

    <h2 style="margin-top: 28px;">Appointment Information</h2>
    <div class="form-grid">
        <div class="form-group">
            <label class="required" for="dentistId">Dentist</label>
            <select id="dentistId" name="dentistId" required>
                <option value="">Select dentist</option>
                <c:forEach var="dentist" items="${dentists}">
                    <option value="${dentist.dentistId}" <c:if test="${form.dentistId == dentist.dentistId}">selected</c:if>>
                        <c:out value="${dentist.dentistName}"/> - <c:out value="${dentist.specialty}"/> (LKR <c:out value="${dentist.consultationFee}"/>)
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label class="required" for="treatmentId">Treatment Type</label>
            <select id="treatmentId" name="treatmentId" required>
                <option value="">Select treatment</option>
                <c:forEach var="treatment" items="${treatments}">
                    <option value="${treatment.treatmentId}" <c:if test="${form.treatmentId == treatment.treatmentId}">selected</c:if>>
                        <c:out value="${treatment.treatmentName}"/> - LKR <c:out value="${treatment.treatmentFee}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label class="required" for="appointmentDate">Appointment Date</label>
            <input id="appointmentDate" name="appointmentDate" type="date" min="${minimumDate}" value="<c:out value='${form.appointmentDate}'/>" required>
        </div>
        <div class="form-group">
            <label class="required" for="appointmentTime">Appointment Time</label>
            <input id="appointmentTime" name="appointmentTime" type="time" value="<c:out value='${form.appointmentTime}'/>" required>
        </div>
        <div class="form-group full">
            <label for="notes">Notes</label>
            <textarea id="notes" name="notes" maxlength="500"><c:out value="${form.notes}"/></textarea>
        </div>
    </div>
    <div class="actions">
        <button class="btn" type="submit">Register Appointment</button>
        <a class="btn secondary" href="${pageContext.request.contextPath}/appointments/">Cancel</a>
    </div>
</form>
<%@ include file="../common/footer.jspf" %>
