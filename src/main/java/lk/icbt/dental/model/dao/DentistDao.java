package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.entity.Dentist;

import java.util.List;

public interface DentistDao {
    Dentist findById(int dentistId) throws Exception;
    List<Dentist> findActive() throws Exception;
}
