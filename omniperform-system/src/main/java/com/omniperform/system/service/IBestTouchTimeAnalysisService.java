package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.BestTouchTimeAnalysis;

public interface IBestTouchTimeAnalysisService {
    int upsert(BestTouchTimeAnalysis data);
    int upsertBatch(List<BestTouchTimeAnalysis> list);
    List<BestTouchTimeAnalysis> listByMonth(String monthYear, String regionCode);
}