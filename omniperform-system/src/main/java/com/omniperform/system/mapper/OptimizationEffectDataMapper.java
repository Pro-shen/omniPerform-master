package com.omniperform.system.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.OptimizationEffectData;

public interface OptimizationEffectDataMapper {
    OptimizationEffectData selectByUnique(@Param("statDate") Date statDate,
                                          @Param("metricName") String metricName,
                                          @Param("regionCode") String regionCode);

    int insertOptimizationEffectData(OptimizationEffectData data);

    int updateOptimizationEffectData(OptimizationEffectData data);

    List<OptimizationEffectData> selectList(OptimizationEffectData filter);
}