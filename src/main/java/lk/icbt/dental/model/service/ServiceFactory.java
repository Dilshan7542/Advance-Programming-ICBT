package lk.icbt.dental.model.service;

import lk.icbt.dental.model.service.impl.*;

public final class ServiceFactory {
    private static final AuthService AUTH_SERVICE = new AuthServiceImpl();
    private static final AppointmentService APPOINTMENT_SERVICE = new AppointmentServiceImpl();
    private static final BillingService BILLING_SERVICE = new BillingServiceImpl();
    private static final DashboardService DASHBOARD_SERVICE = new DashboardServiceImpl();

    private ServiceFactory() {
    }

    public static AuthService authService() { return AUTH_SERVICE; }
    public static AppointmentService appointmentService() { return APPOINTMENT_SERVICE; }
    public static BillingService billingService() { return BILLING_SERVICE; }
    public static DashboardService dashboardService() { return DASHBOARD_SERVICE; }
}
