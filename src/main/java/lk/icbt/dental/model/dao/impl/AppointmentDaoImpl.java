package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.AppointmentDao;
import lk.icbt.dental.model.entity.Appointment;
import lk.icbt.dental.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {
    private static final String BASE_SELECT = """
            SELECT a.appointment_id, a.appointment_no, a.patient_id, a.dentist_id, a.treatment_id,
                   a.appointment_date, a.appointment_time, a.consultation_fee, a.treatment_fee,
                   a.status, a.notes, a.created_at,
                   p.full_name AS patient_name, p.address AS patient_address,
                   p.contact_number, p.email AS patient_email,
                   d.dentist_name, d.specialty AS dentist_specialty,
                   t.treatment_name, b.status AS bill_status
            FROM appointments a
            JOIN patients p ON p.patient_id = a.patient_id
            JOIN dentists d ON d.dentist_id = a.dentist_id
            JOIN treatments t ON t.treatment_id = a.treatment_id
            LEFT JOIN bills b ON b.appointment_id = a.appointment_id
            """;

    @Override
    public int save(Appointment appointment) throws Exception {
        String sql = """
                INSERT INTO appointments
                (appointment_no, patient_id, dentist_id, treatment_id, appointment_date,
                 appointment_time, consultation_fee, treatment_fee, status, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, appointment.getAppointmentNo());
            statement.setInt(2, appointment.getPatientId());
            statement.setInt(3, appointment.getDentistId());
            statement.setInt(4, appointment.getTreatmentId());
            statement.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(6, Time.valueOf(appointment.getAppointmentTime()));
            statement.setBigDecimal(7, appointment.getConsultationFee());
            statement.setBigDecimal(8, appointment.getTreatmentFee());
            statement.setString(9, appointment.getStatus());
            statement.setString(10, appointment.getNotes());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Appointment could not be created");
    }

    @Override
    public Appointment findById(int appointmentId) throws Exception {
        return findOne(BASE_SELECT + " WHERE a.appointment_id = ?", appointmentId);
    }

    @Override
    public Appointment findByAppointmentNo(String appointmentNo) throws Exception {
        return findOne(BASE_SELECT + " WHERE a.appointment_no = ?", appointmentNo);
    }

    private Appointment findOne(String sql, Object value) throws Exception {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (value instanceof Integer integer) {
                statement.setInt(1, integer);
            } else {
                statement.setString(1, String.valueOf(value));
            }
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? map(result) : null;
            }
        }
    }

    @Override
    public boolean existsByAppointmentNo(String appointmentNo) throws Exception {
        String sql = "SELECT 1 FROM appointments WHERE appointment_no = ? LIMIT 1";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, appointmentNo);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    @Override
    public List<Appointment> findAll() throws Exception {
        return findList(BASE_SELECT + " ORDER BY a.appointment_date DESC, a.appointment_time DESC", null);
    }

    @Override
    public List<Appointment> search(String keyword) throws Exception {
        String sql = BASE_SELECT + """
                 WHERE LOWER(a.appointment_no) LIKE ?
                    OR LOWER(p.full_name) LIKE ?
                    OR LOWER(p.contact_number) LIKE ?
                    OR LOWER(d.dentist_name) LIKE ?
                    OR LOWER(t.treatment_name) LIKE ?
                 ORDER BY a.appointment_date DESC, a.appointment_time DESC
                """;
        return findList(sql, keyword == null ? "" : keyword.trim().toLowerCase());
    }

    private List<Appointment> findList(String sql, String keyword) throws Exception {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (keyword != null) {
                String value = "%" + keyword + "%";
                for (int index = 1; index <= 5; index++) {
                    statement.setString(index, value);
                }
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    appointments.add(map(result));
                }
            }
        }
        return appointments;
    }

    @Override
    public boolean isDentistBooked(int dentistId, LocalDate date, LocalTime time) throws Exception {
        String sql = """
                SELECT 1 FROM appointments
                WHERE dentist_id = ? AND appointment_date = ? AND appointment_time = ?
                  AND status <> 'CANCELLED' LIMIT 1
                """;
        return slotExists(sql, dentistId, date, time);
    }

    @Override
    public boolean isPatientBooked(int patientId, LocalDate date, LocalTime time) throws Exception {
        String sql = """
                SELECT 1 FROM appointments
                WHERE patient_id = ? AND appointment_date = ? AND appointment_time = ?
                  AND status <> 'CANCELLED' LIMIT 1
                """;
        return slotExists(sql, patientId, date, time);
    }

    private boolean slotExists(String sql, int id, LocalDate date, LocalTime time) throws Exception {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setDate(2, Date.valueOf(date));
            statement.setTime(3, Time.valueOf(time));
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    @Override
    public boolean updateStatus(int appointmentId, String status) throws Exception {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, appointmentId);
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public long countAll() throws Exception {
        return count("SELECT COUNT(*) FROM appointments", null);
    }

    @Override
    public long countByDate(LocalDate date) throws Exception {
        return count("SELECT COUNT(*) FROM appointments WHERE appointment_date = ?", date);
    }

    @Override
    public long countByStatus(String status) throws Exception {
        return count("SELECT COUNT(*) FROM appointments WHERE status = ?", status);
    }

    private long count(String sql, Object value) throws Exception {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (value instanceof LocalDate date) {
                statement.setDate(1, Date.valueOf(date));
            } else if (value != null) {
                statement.setString(1, String.valueOf(value));
            }
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getLong(1);
            }
        }
    }

    private Appointment map(ResultSet result) throws Exception {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(result.getInt("appointment_id"));
        appointment.setAppointmentNo(result.getString("appointment_no"));
        appointment.setPatientId(result.getInt("patient_id"));
        appointment.setDentistId(result.getInt("dentist_id"));
        appointment.setTreatmentId(result.getInt("treatment_id"));
        appointment.setAppointmentDate(result.getDate("appointment_date").toLocalDate());
        appointment.setAppointmentTime(result.getTime("appointment_time").toLocalTime());
        appointment.setConsultationFee(result.getBigDecimal("consultation_fee"));
        appointment.setTreatmentFee(result.getBigDecimal("treatment_fee"));
        appointment.setStatus(result.getString("status"));
        appointment.setNotes(result.getString("notes"));
        Timestamp createdAt = result.getTimestamp("created_at");
        appointment.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        appointment.setPatientName(result.getString("patient_name"));
        appointment.setPatientAddress(result.getString("patient_address"));
        appointment.setContactNumber(result.getString("contact_number"));
        appointment.setPatientEmail(result.getString("patient_email"));
        appointment.setDentistName(result.getString("dentist_name"));
        appointment.setDentistSpecialty(result.getString("dentist_specialty"));
        appointment.setTreatmentName(result.getString("treatment_name"));
        appointment.setBillStatus(result.getString("bill_status"));
        return appointment;
    }
}
