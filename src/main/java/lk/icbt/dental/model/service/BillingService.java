package lk.icbt.dental.model.service;

import lk.icbt.dental.model.dto.BillView;

public interface BillingService {
    BillView generateBill(int appointmentId) throws Exception;
    boolean updateBillStatus(int appointmentId, String status) throws Exception;
}
