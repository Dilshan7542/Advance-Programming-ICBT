package lk.icbt.dental.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.ClinicFacade;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/appointments/*")
public class AppointmentServlet extends HttpServlet {
    private final ClinicFacade clinic = ClinicFacade.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                showList(request, response);
            } else if ("/add".equals(path)) {
                loadFormData(request);
                request.getRequestDispatcher("/WEB-INF/views/appointments/add.jsp").forward(request, response);
            } else if ("/view".equals(path)) {
                int id = parseId(request);
                request.setAttribute("appointment", clinic.getAppointment(id));
                request.getRequestDispatcher("/WEB-INF/views/appointments/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ValidationException e) {
            request.setAttribute("error", e.getMessage());
            showList(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "The requested appointment operation failed.");
            showList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if ("/add".equals(path)) {
                AppointmentRegistrationRequest form = readForm(request);
                int id = clinic.registerAppointment(form);
                response.sendRedirect(request.getContextPath() + "/appointments/view?id=" + id + "&created=1");
            } else if ("/status".equals(path)) {
                int id = parseId(request);
                clinic.updateAppointmentStatus(id, request.getParameter("status"));
                response.sendRedirect(request.getContextPath() + "/appointments/view?id=" + id + "&updated=1");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ValidationException e) {
            if ("/add".equals(path)) {
                AppointmentRegistrationRequest form = safelyReadForm(request);
                request.setAttribute("form", form);
                request.setAttribute("error", e.getMessage());
                try {
                    loadFormData(request);
                } catch (Exception ignored) {
                    request.setAttribute("error", e.getMessage() + " Reference data could not be loaded.");
                }
                request.getRequestDispatcher("/WEB-INF/views/appointments/add.jsp").forward(request, response);
            } else {
                request.setAttribute("error", e.getMessage());
                try {
                    int id = parseId(request);
                    request.setAttribute("appointment", clinic.getAppointment(id));
                    request.getRequestDispatcher("/WEB-INF/views/appointments/view.jsp").forward(request, response);
                } catch (Exception inner) {
                    response.sendRedirect(request.getContextPath() + "/appointments/");
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Unable to save the appointment. Please verify the database connection.");
            if ("/add".equals(path)) {
                request.setAttribute("form", safelyReadForm(request));
                try {
                    loadFormData(request);
                } catch (Exception ignored) {
                    // Original message is more useful to the user.
                }
                request.getRequestDispatcher("/WEB-INF/views/appointments/add.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/appointments/");
            }
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("q");
        request.setAttribute("q", keyword);
        try {
            request.setAttribute("appointments", clinic.listAppointments(keyword));
        } catch (Exception e) {
            if (request.getAttribute("error") == null) {
                request.setAttribute("error", "Appointments could not be loaded.");
            }
            request.setAttribute("appointments", java.util.Collections.emptyList());
        }
        request.getRequestDispatcher("/WEB-INF/views/appointments/list.jsp").forward(request, response);
    }

    private void loadFormData(HttpServletRequest request) throws Exception {
        request.setAttribute("dentists", clinic.activeDentists());
        request.setAttribute("treatments", clinic.activeTreatments());
        request.setAttribute("minimumDate", LocalDate.now());
    }

    private AppointmentRegistrationRequest readForm(HttpServletRequest request) {
        AppointmentRegistrationRequest form = new AppointmentRegistrationRequest();
        form.setPatientName(request.getParameter("patientName"));
        form.setAddress(request.getParameter("address"));
        form.setContactNumber(request.getParameter("contactNumber"));
        form.setEmail(request.getParameter("email"));
        form.setDentistId(Integer.parseInt(request.getParameter("dentistId")));
        form.setTreatmentId(Integer.parseInt(request.getParameter("treatmentId")));
        form.setAppointmentDate(LocalDate.parse(request.getParameter("appointmentDate")));
        form.setAppointmentTime(LocalTime.parse(request.getParameter("appointmentTime")));
        form.setNotes(request.getParameter("notes"));
        return form;
    }

    private AppointmentRegistrationRequest safelyReadForm(HttpServletRequest request) {
        AppointmentRegistrationRequest form = new AppointmentRegistrationRequest();
        form.setPatientName(request.getParameter("patientName"));
        form.setAddress(request.getParameter("address"));
        form.setContactNumber(request.getParameter("contactNumber"));
        form.setEmail(request.getParameter("email"));
        form.setNotes(request.getParameter("notes"));
        try { form.setDentistId(Integer.parseInt(request.getParameter("dentistId"))); } catch (Exception ignored) { }
        try { form.setTreatmentId(Integer.parseInt(request.getParameter("treatmentId"))); } catch (Exception ignored) { }
        try { form.setAppointmentDate(LocalDate.parse(request.getParameter("appointmentDate"))); } catch (Exception ignored) { }
        try { form.setAppointmentTime(LocalTime.parse(request.getParameter("appointmentTime"))); } catch (Exception ignored) { }
        return form;
    }

    private int parseId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            throw new ValidationException("A valid appointment ID is required.");
        }
    }
}
