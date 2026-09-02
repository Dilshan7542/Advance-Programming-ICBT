package lk.icbt.dental.model.validation;

import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class AppointmentValidator {
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^[0-9+ -]{9,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private AppointmentValidator() {
    }

    public static void validate(AppointmentRegistrationRequest request) {
        if (request == null) {
            throw new ValidationException("Appointment details are required.");
        }
        requireText(request.getPatientName(), "Patient name is required.");
        requireText(request.getAddress(), "Patient address is required.");
        requireText(request.getContactNumber(), "Contact number is required.");

        if (!CONTACT_PATTERN.matcher(request.getContactNumber().trim()).matches()) {
            throw new ValidationException("Enter a valid contact number using 9 to 15 digits.");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !EMAIL_PATTERN.matcher(request.getEmail().trim()).matches()) {
            throw new ValidationException("Enter a valid email address.");
        }
        if (request.getDentistId() <= 0) {
            throw new ValidationException("Dentist is required.");
        }
        if (request.getTreatmentId() <= 0) {
            throw new ValidationException("Treatment type is required.");
        }
        if (request.getAppointmentDate() == null) {
            throw new ValidationException("Appointment date is required.");
        }
        if (request.getAppointmentTime() == null) {
            throw new ValidationException("Appointment time is required.");
        }
        if (request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Appointment date cannot be in the past.");
        }
        LocalDateTime appointmentDateTime = LocalDateTime.of(
                request.getAppointmentDate(), request.getAppointmentTime());
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment date and time cannot be in the past.");
        }
    }

    private static void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
    }
}
