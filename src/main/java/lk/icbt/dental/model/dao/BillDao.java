package lk.icbt.dental.model.dao;

import lk.icbt.dental.model.dto.BillView;
import lk.icbt.dental.model.entity.Bill;

import java.math.BigDecimal;

public interface BillDao {
    Bill findByAppointmentId(int appointmentId) throws Exception;
    BillView findViewByAppointmentId(int appointmentId) throws Exception;
    int save(Bill bill) throws Exception;
    boolean updateStatus(int appointmentId, String status) throws Exception;
    BigDecimal totalPaidRevenue() throws Exception;
}
