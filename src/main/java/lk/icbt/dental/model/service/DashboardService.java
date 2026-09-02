package lk.icbt.dental.model.service;

import lk.icbt.dental.model.dto.DashboardStats;

public interface DashboardService {
    DashboardStats getStats() throws Exception;
}
