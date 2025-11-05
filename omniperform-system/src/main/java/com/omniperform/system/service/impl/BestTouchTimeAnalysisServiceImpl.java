package com.omniperform.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.system.domain.BestTouchTimeAnalysis;
import com.omniperform.system.mapper.BestTouchTimeAnalysisMapper;
import com.omniperform.system.service.IBestTouchTimeAnalysisService;

@Service
public class BestTouchTimeAnalysisServiceImpl implements IBestTouchTimeAnalysisService {
    @Autowired
    private BestTouchTimeAnalysisMapper mapper;

    @Override
    public int upsert(BestTouchTimeAnalysis data) {
        if (data == null) return 0;
        // 兜底monthYear
        if (data.getAnalysisDate() != null && (data.getMonthYear() == null || data.getMonthYear().isEmpty())) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM");
            data.setMonthYear(sdf.format(data.getAnalysisDate()));
        }
        BestTouchTimeAnalysis existing = mapper.selectByUnique(data.getAnalysisDate(), data.getTimeSlot(), data.getRegionCode());
        if (existing != null && existing.getId() != null) {
            data.setId(existing.getId());
            data.setUpdateTime(new Date());
            return mapper.updateBestTouchTimeAnalysis(data);
        } else {
            data.setCreateTime(new Date());
            data.setUpdateTime(new Date());
            return mapper.insertBestTouchTimeAnalysis(data);
        }
    }

    @Override
    public int upsertBatch(List<BestTouchTimeAnalysis> list) {
        int count = 0;
        if (list == null) return count;
        for (BestTouchTimeAnalysis d : list) {
            count += upsert(d);
        }
        return count;
    }

    @Override
    public List<BestTouchTimeAnalysis> listByMonth(String monthYear, String regionCode) {
        BestTouchTimeAnalysis filter = new BestTouchTimeAnalysis();
        filter.setMonthYear(monthYear);
        filter.setRegionCode(regionCode);
        return mapper.selectList(filter);
    }
}