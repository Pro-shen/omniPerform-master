package com.omniperform.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.DashboardOverviewKpi;

public interface DashboardOverviewKpiMapper {
    DashboardOverviewKpi selectByUnique(@Param("dataMonth") String dataMonth,
                                        @Param("regionCode") String regionCode);

    int insertDashboardOverviewKpi(DashboardOverviewKpi data);

    int updateDashboardOverviewKpi(DashboardOverviewKpi data);

    List<DashboardOverviewKpi> selectList(DashboardOverviewKpi filter);
}