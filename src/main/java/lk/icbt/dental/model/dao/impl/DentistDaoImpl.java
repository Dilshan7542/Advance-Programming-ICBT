package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.DentistDao;
import lk.icbt.dental.model.entity.Dentist;
import lk.icbt.dental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DentistDaoImpl implements DentistDao {
    @Override
    public Dentist findById(int dentistId) throws Exception {
        String sql = "SELECT dentist_id, dentist_name, specialty, consultation_fee, active FROM dentists WHERE dentist_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dentistId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? map(result) : null;
            }
        }
    }

    @Override
    public List<Dentist> findActive() throws Exception {
        String sql = "SELECT dentist_id, dentist_name, specialty, consultation_fee, active FROM dentists WHERE active = TRUE ORDER BY dentist_name";
        List<Dentist> dentists = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                dentists.add(map(result));
            }
        }
        return dentists;
    }

    private Dentist map(ResultSet result) throws Exception {
        Dentist dentist = new Dentist();
        dentist.setDentistId(result.getInt("dentist_id"));
        dentist.setDentistName(result.getString("dentist_name"));
        dentist.setSpecialty(result.getString("specialty"));
        dentist.setConsultationFee(result.getBigDecimal("consultation_fee"));
        dentist.setActive(result.getBoolean("active"));
        return dentist;
    }
}
