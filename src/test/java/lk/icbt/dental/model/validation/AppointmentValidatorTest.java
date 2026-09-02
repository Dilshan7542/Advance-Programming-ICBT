package lk.icbt.dental.model.validation;

import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentValidatorTest {
    @Test
    void shouldAcceptValidAppointmentData() {
        AppointmentRegistrationRequest request = validRequest();
        assertDoesNotThrow(() -> AppointmentValidator.validate(request));
    }

    @Test
    void shouldRejectMissingPatientName() {
        AppointmentRegistrationRequest request = validRequest();
        request.setPatientName(" ");
        assertThrows(ValidationException.class, () -> AppointmentValidator.validate(request));
    }

    @Test
    void shouldRejectPastAppointment() {
        AppointmentRegistrationRequest request = validRequest();
        request.setAppointmentDate(LocalDate.now().minusDays(1));
        assertThrows(ValidationException.class, () -> AppointmentValidator.validate(request));
    }

    @Test
    void shouldRejectInvalidContactNumber() {
        AppointmentRegistrationRequest request = validRequest();
        request.setContactNumber("ABC");
        assertThrows(ValidationException.class, () -> AppointmentValidator.validate(request));
    }

    private AppointmentRegistrationRequest validRequest() {
        AppointmentRegistrationRequest request = new AppointmentRegistrationRequest();
        request.setPatientName("Test Patient");
        request.setAddress("10 Main Street, Colombo");
        request.setContactNumber("0771234567");
        request.setEmail("patient@example.com");
        request.setDentistId(1);
        request.setTreatmentId(1);
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setAppointmentTime(LocalTime.of(10, 30));
        return request;
    }
}
