package lk.icbt.dental.controller.api;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.dental.model.entity.Appointment;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.ClinicFacade;
import lk.icbt.dental.util.JsonUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/appointments")
public class AppointmentApiServlet extends HttpServlet {
    private final ClinicFacade clinic = ClinicFacade.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Appointment appointment = clinic.findAppointment(request.getParameter("appointmentNo"));
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("appointmentNo", appointment.getAppointmentNo());
            payload.put("patientName", appointment.getPatientName());
            payload.put("address", appointment.getPatientAddress());
            payload.put("contactNumber", appointment.getContactNumber());
            payload.put("dentistName", appointment.getDentistName());
            payload.put("treatmentType", appointment.getTreatmentName());
            payload.put("appointmentDate", appointment.getAppointmentDate().toString());
            payload.put("appointmentTime", appointment.getAppointmentTime().toString());
            payload.put("status", appointment.getStatus());
            response.getWriter().write(JsonUtil.toJson(payload));
        } catch (ValidationException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(JsonUtil.toJson(Map.of("error", e.getMessage())));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(JsonUtil.toJson(Map.of("error", "Unable to retrieve appointment.")));
        }
    }
}
