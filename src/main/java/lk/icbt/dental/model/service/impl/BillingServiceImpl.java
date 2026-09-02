package lk.icbt.dental.model.service.impl;

import lk.icbt.dental.model.dao.DaoFactory;
import lk.icbt.dental.model.dto.BillView;
import lk.icbt.dental.model.entity.Appointment;
import lk.icbt.dental.model.entity.Bill;
import lk.icbt.dental.model.exception.ValidationException;
import lk.icbt.dental.model.service.BillingService;
import lk.icbt.dental.model.strategy.BillingStrategy;
import lk.icbt.dental.model.strategy.StandardBillingStrategy;
import lk.icbt.dental.util.AppConstants;

import java.util.Set;

public class BillingServiceImpl implements BillingService {
    private static final Set<String> ALLOWED_BILL_STATUSES = Set.of(
            AppConstants.BILL_UNPAID, AppConstants.BILL_PAID
    );
    private final BillingStrategy billingStrategy = new StandardBillingStrategy();

    @Override
    public BillView generateBill(int appointmentId) throws Exception {
        if (appointmentId <= 0) {
            throw new ValidationException("Appointment ID is required.");
        }

        Bill bill = DaoFactory.billDao().findByAppointmentId(appointmentId);
        if (bill == null) {
            Appointment appointment = DaoFactory.appointmentDao().findById(appointmentId);
            if (appointment == null) {
                throw new ValidationException("Appointment was not found.");
            }
            if (AppConstants.STATUS_CANCELLED.equalsIgnoreCase(appointment.getStatus())) {
                throw new ValidationException("A bill cannot be created for a cancelled appointment.");
            }

            bill = new Bill();
            bill.setAppointmentId(appointmentId);
            bill.setConsultationFee(appointment.getConsultationFee());
            bill.setTreatmentFee(appointment.getTreatmentFee());
            bill.setTotalAmount(billingStrategy.calculateTotal(
                    appointment.getConsultationFee(), appointment.getTreatmentFee()));
            bill.setStatus(AppConstants.BILL_UNPAID);
            DaoFactory.billDao().save(bill);
        }

        BillView view = DaoFactory.billDao().findViewByAppointmentId(appointmentId);
        if (view == null) {
            throw new ValidationException("Unable to load bill details.");
        }
        return view;
    }

    @Override
    public boolean updateBillStatus(int appointmentId, String status) throws Exception {
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!ALLOWED_BILL_STATUSES.contains(normalized)) {
            throw new ValidationException("Invalid bill status.");
        }
        generateBill(appointmentId);
        return DaoFactory.billDao().updateStatus(appointmentId, normalized);
    }
}
