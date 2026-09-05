package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.entity.Patient;

public interface PatientDao {
    Patient saveOrUpdateByContact(Patient patient) throws Exception;
    Patient findByContactNumber(String contactNumber) throws Exception;
    long countAll() throws Exception;
}
