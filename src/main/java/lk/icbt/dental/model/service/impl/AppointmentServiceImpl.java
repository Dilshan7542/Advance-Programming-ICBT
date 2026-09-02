package lk.icbt.dental.model.service.impl;

import lk.icbt.dental.model.dao.DaoFactory;
import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.entity.*;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.AppointmentService;
import lk.icbt.dental.model.validation.AppointmentValidator;
import lk.icbt.dental.util.AppConstants;
import lk.icbt.dental.util.AppointmentNumberGenerator;

import java.util.List;
import java.util.Set;

public class AppointmentServiceImpl implements AppointmentService {
    private static final Set<String> ALLOWED_STATUSES = Set.of(
            AppConstants.STATUS_SCHEDULED,
            AppConstants.STATUS_COMPLETED,
            AppConstants.STATUS_CANCELLED
    );

    @Override
    public int register(AppointmentRegistrationRequest request) throws Exception {
        AppointmentValidator.validate(request);

        Dentist dentist = DaoFactory.dentistDao().findById(request.getDentistId());
        if (dentist == null || !dentist.isActive()) {
            throw new ValidationException("The selected dentist is unavailable.");
        }

        Treatment treatment = DaoFactory.treatmentDao().findById(request.getTreatmentId());
        if (treatment == null || !treatment.isActive()) {
            throw new ValidationException("The selected treatment is unavailable.");
        }

        Patient patient = new Patient();
        patient.setFullName(request.getPatientName().trim());
        patient.setAddress(request.getAddress().trim());
        patient.setContactNumber(normalizeContact(request.getContactNumber()));
        patient.setEmail(blankToNull(request.getEmail()));
        patient = DaoFactory.patientDao().saveOrUpdateByContact(patient);

        if (DaoFactory.appointmentDao().isDentistBooked(
                dentist.getDentistId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new ValidationException("The selected dentist is already booked for this date and time.");
        }
        if (DaoFactory.appointmentDao().isPatientBooked(
                patient.getPatientId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new ValidationException("This patient already has an appointment for this date and time.");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentNo(createUniqueAppointmentNumber(request));
        appointment.setPatientId(patient.getPatientId());
        appointment.setDentistId(dentist.getDentistId());
        appointment.setTreatmentId(treatment.getTreatmentId());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setConsultationFee(dentist.getConsultationFee());
        appointment.setTreatmentFee(treatment.getTreatmentFee());
        appointment.setStatus(AppConstants.STATUS_SCHEDULED);
        appointment.setNotes(blankToNull(request.getNotes()));
        return DaoFactory.appointmentDao().save(appointment);
    }

    private String createUniqueAppointmentNumber(AppointmentRegistrationRequest request) throws Exception {
        for (int attempt = 0; attempt < 10; attempt++) {
            String value = AppointmentNumberGenerator.generate(request.getAppointmentDate());
            if (!DaoFactory.appointmentDao().existsByAppointmentNo(value)) {
                return value;
            }
        }
        throw new ValidationException("Unable to generate a unique appointment number. Please retry.");
    }

    @Override
    public Appointment getById(int appointmentId) throws Exception {
        if (appointmentId <= 0) {
            throw new ValidationException("Appointment ID is required.");
        }
        Appointment appointment = DaoFactory.appointmentDao().findById(appointmentId);
        if (appointment == null) {
            throw new ValidationException("Appointment was not found.");
        }
        return appointment;
    }

    @Override
    public Appointment getByAppointmentNo(String appointmentNo) throws Exception {
        if (appointmentNo == null || appointmentNo.isBlank()) {
            throw new ValidationException("Appointment number is required.");
        }
        Appointment appointment = DaoFactory.appointmentDao().findByAppointmentNo(appointmentNo.trim());
        if (appointment == null) {
            throw new ValidationException("No appointment was found for the provided appointment number.");
        }
        return appointment;
    }

    @Override
    public List<Appointment> getAll() throws Exception {
        return DaoFactory.appointmentDao().findAll();
    }

    @Override
    public List<Appointment> search(String keyword) throws Exception {
        return DaoFactory.appointmentDao().search(keyword);
    }

    @Override
    public List<Dentist> getActiveDentists() throws Exception {
        return DaoFactory.dentistDao().findActive();
    }

    @Override
    public List<Treatment> getActiveTreatments() throws Exception {
        return DaoFactory.treatmentDao().findActive();
    }

    @Override
    public boolean updateStatus(int appointmentId, String status) throws Exception {
        Appointment current = getById(appointmentId);
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalized)) {
            throw new ValidationException("Invalid appointment status.");
        }
        if (AppConstants.STATUS_CANCELLED.equals(current.getStatus())
                && !AppConstants.STATUS_CANCELLED.equals(normalized)) {
            throw new ValidationException("A cancelled appointment cannot be reactivated.");
        }
        return DaoFactory.appointmentDao().updateStatus(appointmentId, normalized);
    }

    private String normalizeContact(String value) {
        return value == null ? null : value.replace(" ", "").replace("-", "");
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
