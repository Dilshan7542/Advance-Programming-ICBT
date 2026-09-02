package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.entity.Treatment;

import java.util.List;

public interface TreatmentDao {
    Treatment findById(int treatmentId) throws Exception;
    List<Treatment> findActive() throws Exception;
}
