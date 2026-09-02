package lk.icbt.dental.model.service;

import lk.icbt.dental.model.dto.AppointmentRegistrationRequest;
import lk.icbt.dental.model.dto.BillView;
import lk.icbt.dental.model.dto.DashboardStats;
import lk.icbt.dental.model.entity.Appointment;
import lk.icbt.dental.model.entity.Dentist;
import lk.icbt.dental.model.entity.Treatment;

import java.util.List;

/**
 * Facade pattern: provides controllers with a single entry point to clinic
 * use cases without exposing DAO or service implementation details.
 */
public final class ClinicFacade {
    private final AppointmentService appointmentService = ServiceFactory.appointmentService();
    private final BillingService billingService = ServiceFactory.billingService();
    private final DashboardService dashboardService = ServiceFactory.dashboardService();

    private ClinicFacade() {
    }

    private static final class Holder {
        private static final ClinicFacade INSTANCE = new ClinicFacade();
    }

    public static ClinicFacade getInstance() {
        return Holder.INSTANCE;
    }

    public int registerAppointment(AppointmentRegistrationRequest request) throws Exception {
        return appointmentService.register(request);
    }

    public Appointment getAppointment(int id) throws Exception {
        return appointmentService.getById(id);
    }

    public Appointment findAppointment(String appointmentNo) throws Exception {
        return appointmentService.getByAppointmentNo(appointmentNo);
    }

    public List<Appointment> listAppointments(String keyword) throws Exception {
        return keyword == null || keyword.isBlank()
                ? appointmentService.getAll()
                : appointmentService.search(keyword);
    }

    public List<Dentist> activeDentists() throws Exception {
        return appointmentService.getActiveDentists();
    }

    public List<Treatment> activeTreatments() throws Exception {
        return appointmentService.getActiveTreatments();
    }

    public boolean updateAppointmentStatus(int id, String status) throws Exception {
        return appointmentService.updateStatus(id, status);
    }

    public BillView generateBill(int appointmentId) throws Exception {
        return billingService.generateBill(appointmentId);
    }

    public boolean updateBillStatus(int appointmentId, String status) throws Exception {
        return billingService.updateBillStatus(appointmentId, status);
    }

    public DashboardStats dashboardStats() throws Exception {
        return dashboardService.getStats();
    }
}
