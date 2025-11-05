package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.omniperform.system.domain.DashboardOverviewKpi;
import com.omniperform.system.mapper.DashboardOverviewKpiMapper;
import com.omniperform.system.service.IDashboardOverviewKpiService;

@Service
public class DashboardOverviewKpiServiceImpl implements IDashboardOverviewKpiService {
    @Autowired
    private DashboardOverviewKpiMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(DashboardOverviewKpiServiceImpl.class);

    @Override
    public int upsert(DashboardOverviewKpi data) {
        if (data == null) return 0;
        log.info("[OverviewKpiService] upsert: dataMonth={}, regionCode={}", data.getDataMonth(), data.getRegionCode());
        DashboardOverviewKpi existing = mapper.selectByUnique(data.getDataMonth(), data.getRegionCode());
        if (existing != null && existing.getId() != null) {
            data.setId(existing.getId());
            data.setUpdateTime(new Date());
            int rows = mapper.updateDashboardOverviewKpi(data);
            log.info("[OverviewKpiService] updateDashboardOverviewKpi affectedRows={}", rows);
            return rows;
        } else {
            data.setCreateTime(new Date());
            data.setUpdateTime(new Date());
            int rows = mapper.insertDashboardOverviewKpi(data);
            log.info("[OverviewKpiService] insertDashboardOverviewKpi affectedRows={}", rows);
            return rows;
        }
    }

    @Override
    public int upsertBatch(List<DashboardOverviewKpi> list) {
        int count = 0;
        if (list == null) return count;
        log.info("[OverviewKpiService] upsertBatch size={}", list.size());
        for (DashboardOverviewKpi d : list) {
            count += upsert(d);
        }
        log.info("[OverviewKpiService] upsertBatch totalAffected={}", count);
        return count;
    }

    @Override
    public List<DashboardOverviewKpi> listByMonth(String monthYear, String regionCode) {
        DashboardOverviewKpi filter = new DashboardOverviewKpi();
        filter.setDataMonth(monthYear);
        filter.setRegionCode(regionCode);
        List<DashboardOverviewKpi> result = mapper.selectList(filter);
        log.info("[OverviewKpiService] listByMonth monthYear={}, regionCode={}, resultSize={}",
                 monthYear, regionCode, (result == null ? 0 : result.size()));
        return result;
    }
}