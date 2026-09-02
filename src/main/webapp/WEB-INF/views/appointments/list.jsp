<% request.setAttribute("pageTitle", "Appointments - Sunrise Dental Clinic"); %>
<%@ include file="../common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Appointments</h1>
        <p class="subtitle">Search by appointment number, patient, contact, dentist or treatment.</p>
    </div>
    <a class="btn" href="${pageContext.request.contextPath}/appointments/add">New Appointment</a>
</div>

<c:if test="${not empty error}">
    <div class="alert error"><c:out value="${error}"/></div>
</c:if>

<section class="card">
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/appointments/">
        <input type="text" name="q" value="<c:out value='${q}'/>" placeholder="Enter appointment number or patient details">
        <button class="btn" type="submit">Search</button>
        <a class="btn secondary" href="${pageContext.request.contextPath}/appointments/">Clear</a>
    </form>
</section>

<section class="card table-wrap">
    <table>
        <thead>
        <tr>
            <th>Appointment No.</th>
            <th>Patient</th>
            <th>Dentist</th>
            <th>Treatment</th>
            <th>Date / Time</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${appointments}">
            <tr>
                <td><strong><c:out value="${item.appointmentNo}"/></strong></td>
                <td><c:out value="${item.patientName}"/><br><small><c:out value="${item.contactNumber}"/></small></td>
                <td><c:out value="${item.dentistName}"/></td>
                <td><c:out value="${item.treatmentName}"/></td>
                <td><c:out value="${item.appointmentDate}"/><br><c:out value="${item.appointmentTime}"/></td>
                <td><span class="badge ${item.status}"><c:out value="${item.status}"/></span></td>
                <td>
                    <a class="btn small secondary" href="${pageContext.request.contextPath}/appointments/view?id=${item.appointmentId}">View</a>
                    <c:if test="${item.status != 'CANCELLED'}">
                        <a class="btn small" href="${pageContext.request.contextPath}/billing?id=${item.appointmentId}">Bill</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty appointments}">
            <tr><td colspan="7">No appointment records were found.</td></tr>
        </c:if>
        </tbody>
    </table>
</section>
<%@ include file="../common/footer.jspf" %>
