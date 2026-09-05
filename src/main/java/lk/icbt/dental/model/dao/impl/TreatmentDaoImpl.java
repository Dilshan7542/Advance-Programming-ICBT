package lk.icbt.dental.model.dao.impl;

import lk.icbt.dental.model.dao.TreatmentDao;
import lk.icbt.dental.model.entity.Treatment;
import lk.icbt.dental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDaoImpl implements TreatmentDao {
    @Override
    public Treatment findById(int treatmentId) throws Exception {
        String sql = "SELECT treatment_id, treatment_code, treatment_name, treatment_fee, active FROM treatments WHERE treatment_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, treatmentId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? map(result) : null;
            }
        }
    }

    @Override
    public List<Treatment> findActive() throws Exception {
        String sql = "SELECT treatment_id, treatment_code, treatment_name, treatment_fee, active FROM treatments WHERE active = TRUE ORDER BY treatment_name";
        List<Treatment> treatments = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                treatments.add(map(result));
            }
        }
        return treatments;
    }

    private Treatment map(ResultSet result) throws Exception {
        Treatment treatment = new Treatment();
        treatment.setTreatmentId(result.getInt("treatment_id"));
        treatment.setTreatmentCode(result.getString("treatment_code"));
        treatment.setTreatmentName(result.getString("treatment_name"));
        treatment.setTreatmentFee(result.getBigDecimal("treatment_fee"));
        treatment.setActive(result.getBoolean("active"));
        return treatment;
    }
}
