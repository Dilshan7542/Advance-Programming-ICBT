package lk.icbt.dental.model.service;

import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.entity.Appointment;
import lk.icbt.dental.model.entity.Dentist;
import lk.icbt.dental.model.entity.Treatment;

import java.util.List;

public interface AppointmentService {
    int register(AppointmentRegistrationRequest request) throws Exception;
    Appointment getById(int appointmentId) throws Exception;
    Appointment getByAppointmentNo(String appointmentNo) throws Exception;
    List<Appointment> getAll() throws Exception;
    List<Appointment> search(String keyword) throws Exception;
    List<Dentist> getActiveDentists() throws Exception;
    List<Treatment> getActiveTreatments() throws Exception;
    boolean updateStatus(int appointmentId, String status) throws Exception;
}
