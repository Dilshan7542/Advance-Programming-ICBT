<% request.setAttribute("pageTitle", "Help - Sunrise Dental Clinic"); %>
<%@ include file="common/header.jspf" %>
<div class="page-header">
    <div>
        <h1>Help Section</h1>
        <p class="subtitle">Step-by-step instructions for clinic staff.</p>
    </div>
</div>

<section class="card">
    <div class="help-step"><div class="step-number">1</div><div><h3>Login</h3><p>Enter your authorized username and password. Contact the manager if access is denied.</p></div></div>
    <div class="help-step"><div class="step-number">2</div><div><h3>Register an Appointment</h3><p>Select <strong>New Appointment</strong>, enter the patient's name, address and contact number, then select the dentist, treatment, date and time. The system generates a unique appointment number.</p></div></div>
    <div class="help-step"><div class="step-number">3</div><div><h3>Search Appointment Details</h3><p>Open <strong>Appointments</strong> and enter the appointment number. You may also search by patient name, phone number, dentist or treatment.</p></div></div>
    <div class="help-step"><div class="step-number">4</div><div><h3>Calculate and Print a Bill</h3><p>Open the appointment and select <strong>Calculate / Print Bill</strong>. The total is the treatment fee plus the consultation fee. Mark the bill as paid and print the PDF receipt.</p></div></div>
    <div class="help-step"><div class="step-number">5</div><div><h3>Update Appointment Status</h3><p>A scheduled appointment can be marked as completed or cancelled. Cancelled appointments cannot be billed.</p></div></div>
    <div class="help-step"><div class="step-number">6</div><div><h3>Exit Safely</h3><p>Select <strong>Exit</strong> from the top menu. This ends the session and returns to the login page.</p></div></div>
</section>

<section class="card">
    <h2>Web Service Test</h2>
    <p>Health endpoint: <code>${pageContext.request.contextPath}/api/health</code></p>
    <p>Appointment lookup: <code>${pageContext.request.contextPath}/api/appointments?appointmentNo=YOUR_APPOINTMENT_NO</code></p>
</section>
<%@ include file="common/footer.jspf" %>
