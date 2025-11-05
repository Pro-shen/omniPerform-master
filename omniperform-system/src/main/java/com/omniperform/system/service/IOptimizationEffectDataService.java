package com.omniperform.system.service;

import java.util.List;
import com.omniperform.system.domain.OptimizationEffectData;

public interface IOptimizationEffectDataService {
    int upsert(OptimizationEffectData data);
    int upsertBatch(List<OptimizationEffectData> list);
    List<OptimizationEffectData> listByMonth(String monthYear, String regionCode);
}