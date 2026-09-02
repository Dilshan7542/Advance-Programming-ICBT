package lk.icbt.dental.model.service.impl;

import lk.icbt.dental.model.dao.DaoFactory;
import lk.icbt.dental.model.dto.DashboardStats;
import lk.icbt.dental.model.service.DashboardService;
import lk.icbt.dental.util.AppConstants;

import java.time.LocalDate;

public class DashboardServiceImpl implements DashboardService {
    @Override
    public DashboardStats getStats() throws Exception {
        DashboardStats stats = new DashboardStats();
        stats.setTotalPatients(DaoFactory.patientDao().countAll());
        stats.setTotalAppointments(DaoFactory.appointmentDao().countAll());
        stats.setTodayAppointments(DaoFactory.appointmentDao().countByDate(LocalDate.now()));
        stats.setScheduledAppointments(
                DaoFactory.appointmentDao().countByStatus(AppConstants.STATUS_SCHEDULED));
        stats.setPaidRevenue(DaoFactory.billDao().totalPaidRevenue());
        return stats;
    }
}
