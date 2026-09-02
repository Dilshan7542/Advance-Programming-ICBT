package lk.icbt.dental.model.dto;

import java.math.BigDecimal;

public class DashboardStats {
    private long totalPatients;
    private long totalAppointments;
    private long todayAppointments;
    private long scheduledAppointments;
    private BigDecimal paidRevenue = BigDecimal.ZERO;

    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }
    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }
    public long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(long todayAppointments) { this.todayAppointments = todayAppointments; }
    public long getScheduledAppointments() { return scheduledAppointments; }
    public void setScheduledAppointments(long scheduledAppointments) { this.scheduledAppointments = scheduledAppointments; }
    public BigDecimal getPaidRevenue() { return paidRevenue; }
    public void setPaidRevenue(BigDecimal paidRevenue) { this.paidRevenue = paidRevenue; }
}
