package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.dao.impl.*;

public final class DaoFactory {
    private static final UserDao USER_DAO = new UserDaoImpl();
    private static final PatientDao PATIENT_DAO = new PatientDaoImpl();
    private static final DentistDao DENTIST_DAO = new DentistDaoImpl();
    private static final TreatmentDao TREATMENT_DAO = new TreatmentDaoImpl();
    private static final AppointmentDao APPOINTMENT_DAO = new AppointmentDaoImpl();
    private static final BillDao BILL_DAO = new BillDaoImpl();

    private DaoFactory() {
    }

    public static UserDao userDao() { return USER_DAO; }
    public static PatientDao patientDao() { return PATIENT_DAO; }
    public static DentistDao dentistDao() { return DENTIST_DAO; }
    public static TreatmentDao treatmentDao() { return TREATMENT_DAO; }
    public static AppointmentDao appointmentDao() { return APPOINTMENT_DAO; }
    public static BillDao billDao() { return BILL_DAO; }
}
