package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentDao {
    int save(Appointment appointment) throws Exception;
    Appointment findById(int appointmentId) throws Exception;
    Appointment findByAppointmentNo(String appointmentNo) throws Exception;
    boolean existsByAppointmentNo(String appointmentNo) throws Exception;
    List<Appointment> findAll() throws Exception;
    List<Appointment> search(String keyword) throws Exception;
    boolean isDentistBooked(int dentistId, LocalDate date, LocalTime time) throws Exception;
    boolean isPatientBooked(int patientId, LocalDate date, LocalTime time) throws Exception;
    boolean updateStatus(int appointmentId, String status) throws Exception;
    long countAll() throws Exception;
    long countByDate(LocalDate date) throws Exception;
    long countByStatus(String status) throws Exception;
}
