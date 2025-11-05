package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.DashboardOverviewKpi;

public interface IDashboardOverviewKpiService {
    int upsert(DashboardOverviewKpi data);
    int upsertBatch(List<DashboardOverviewKpi> list);
    List<DashboardOverviewKpi> listByMonth(String monthYear, String regionCode);
}