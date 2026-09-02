package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.BillDao;
import lk.icbt.dental.model.dto.BillView;
import lk.icbt.dental.model.entity.Bill;
import lk.icbt.dental.util.AppConstants;
import lk.icbt.dental.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class BillDaoImpl implements BillDao {
    @Override
    public Bill findByAppointmentId(int appointmentId) throws Exception {
        String sql = """
                SELECT bill_id, appointment_id, consultation_fee, treatment_fee,
                       total_amount, status, paid_at, created_at
                FROM bills WHERE appointment_id = ?
                """;
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointmentId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                Bill bill = new Bill();
                bill.setBillId(result.getInt("bill_id"));
                bill.setAppointmentId(result.getInt("appointment_id"));
                bill.setConsultationFee(result.getBigDecimal("consultation_fee"));
                bill.setTreatmentFee(result.getBigDecimal("treatment_fee"));
                bill.setTotalAmount(result.getBigDecimal("total_amount"));
                bill.setStatus(result.getString("status"));
                Timestamp paidAt = result.getTimestamp("paid_at");
                Timestamp createdAt = result.getTimestamp("created_at");
                bill.setPaidAt(paidAt == null ? null : paidAt.toLocalDateTime());
                bill.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
                return bill;
            }
        }
    }

    @Override
    public BillView findViewByAppointmentId(int appointmentId) throws Exception {
        String sql = """
                SELECT b.bill_id, b.appointment_id, b.consultation_fee, b.treatment_fee,
                       b.total_amount, b.status, b.paid_at,
                       a.appointment_no, a.appointment_date, a.appointment_time,
                       p.full_name AS patient_name, p.contact_number,
                       d.dentist_name, t.treatment_name
                FROM bills b
                JOIN appointments a ON a.appointment_id = b.appointment_id
                JOIN patients p ON p.patient_id = a.patient_id
                JOIN dentists d ON d.dentist_id = a.dentist_id
                JOIN treatments t ON t.treatment_id = a.treatment_id
                WHERE b.appointment_id = ?
                """;
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointmentId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                BillView view = new BillView();
                view.setBillId(result.getInt("bill_id"));
                view.setAppointmentId(result.getInt("appointment_id"));
                view.setAppointmentNo(result.getString("appointment_no"));
                view.setPatientName(result.getString("patient_name"));
                view.setContactNumber(result.getString("contact_number"));
                view.setDentistName(result.getString("dentist_name"));
                view.setTreatmentName(result.getString("treatment_name"));
                view.setAppointmentDate(result.getDate("appointment_date").toLocalDate());
                view.setAppointmentTime(result.getTime("appointment_time").toLocalTime());
                view.setConsultationFee(result.getBigDecimal("consultation_fee"));
                view.setTreatmentFee(result.getBigDecimal("treatment_fee"));
                view.setTotalAmount(result.getBigDecimal("total_amount"));
                view.setStatus(result.getString("status"));
                Timestamp paidAt = result.getTimestamp("paid_at");
                view.setPaidAt(paidAt == null ? null : paidAt.toLocalDateTime());
                return view;
            }
        }
    }

    @Override
    public int save(Bill bill) throws Exception {
        String sql = """
                INSERT INTO bills
                (appointment_id, consultation_fee, treatment_fee, total_amount, status)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, bill.getAppointmentId());
            statement.setBigDecimal(2, bill.getConsultationFee());
            statement.setBigDecimal(3, bill.getTreatmentFee());
            statement.setBigDecimal(4, bill.getTotalAmount());
            statement.setString(5, bill.getStatus());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    @Override
    public boolean updateStatus(int appointmentId, String status) throws Exception {
        String sql = "UPDATE bills SET status = ?, paid_at = ? WHERE appointment_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            if (AppConstants.BILL_PAID.equals(status)) {
                statement.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            } else {
                statement.setNull(2, Types.TIMESTAMP);
            }
            statement.setInt(3, appointmentId);
            return statement.executeUpdate() == 1;
        }
    }

    @Override
    public BigDecimal totalPaidRevenue() throws Exception {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills WHERE status = 'PAID'";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getBigDecimal(1);
        }
    }
}
