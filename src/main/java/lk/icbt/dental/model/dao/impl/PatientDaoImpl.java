package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.PatientDao;
import lk.icbt.dental.model.entity.Patient;
import lk.icbt.dental.util.DBConnection;

import java.sql.*;

public class PatientDaoImpl implements PatientDao {
    @Override
    public Patient saveOrUpdateByContact(Patient patient) throws Exception {
        String sql = """
                INSERT INTO patients (full_name, address, contact_number, email)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                  patient_id = LAST_INSERT_ID(patient_id),
                  full_name = VALUES(full_name),
                  address = VALUES(address),
                  email = VALUES(email)
                """;
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getFullName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());
            statement.setString(4, patient.getEmail());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    patient.setPatientId(keys.getInt(1));
                    return patient;
                }
            }
        }
        Patient existing = findByContactNumber(patient.getContactNumber());
        if (existing == null) {
            throw new SQLException("Unable to create or locate patient record");
        }
        return existing;
    }

    @Override
    public Patient findByContactNumber(String contactNumber) throws Exception {
        String sql = "SELECT patient_id, full_name, address, contact_number, email FROM patients WHERE contact_number = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contactNumber);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                Patient patient = new Patient();
                patient.setPatientId(result.getInt("patient_id"));
                patient.setFullName(result.getString("full_name"));
                patient.setAddress(result.getString("address"));
                patient.setContactNumber(result.getString("contact_number"));
                patient.setEmail(result.getString("email"));
                return patient;
            }
        }
    }

    @Override
    public long countAll() throws Exception {
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM patients");
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getLong(1);
        }
    }
}
