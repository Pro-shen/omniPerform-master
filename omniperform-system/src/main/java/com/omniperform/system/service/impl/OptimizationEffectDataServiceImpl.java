package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.domain.OptimizationEffectData;
import com.omniperform.system.mapper.OptimizationEffectDataMapper;
import com.omniperform.system.service.IOptimizationEffectDataService;

@Service
public class OptimizationEffectDataServiceImpl implements IOptimizationEffectDataService {
    @Autowired
    private OptimizationEffectDataMapper mapper;

    @Override
    public int upsert(OptimizationEffectData data) {
        if (data == null) return 0;
        // 兜底monthYear
        if (data.getStatDate() != null && (data.getMonthYear() == null || data.getMonthYear().isEmpty())) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM");
            data.setMonthYear(sdf.format(data.getStatDate()));
        }
        OptimizationEffectData existing = mapper.selectByUnique(data.getStatDate(), data.getMetricName(), data.getRegionCode());
        if (existing != null && existing.getId() != null) {
            data.setId(existing.getId());
            data.setUpdateTime(new Date());
            return mapper.updateOptimizationEffectData(data);
        } else {
            data.setCreateTime(new Date());
            data.setUpdateTime(new Date());
            return mapper.insertOptimizationEffectData(data);
        }
    }

    @Override
    public int upsertBatch(List<OptimizationEffectData> list) {
        int count = 0;
        if (list == null) return count;
        for (OptimizationEffectData d : list) {
            count += upsert(d);
        }
        return count;
    }

    @Override
    public List<OptimizationEffectData> listByMonth(String monthYear, String regionCode) {
        OptimizationEffectData filter = new OptimizationEffectData();
        filter.setMonthYear(monthYear);
        filter.setRegionCode(regionCode);
        return mapper.selectList(filter);
    }
}