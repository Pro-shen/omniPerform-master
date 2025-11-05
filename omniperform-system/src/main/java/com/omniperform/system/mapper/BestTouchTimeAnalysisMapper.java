package com.omniperform.system.mapper;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.omniperform.system.domain.BestTouchTimeAnalysis;

public interface BestTouchTimeAnalysisMapper {
    BestTouchTimeAnalysis selectByUnique(@Param("analysisDate") Date analysisDate,
                                         @Param("timeSlot") String timeSlot,
                                         @Param("regionCode") String regionCode);

    int insertBestTouchTimeAnalysis(BestTouchTimeAnalysis data);

    int updateBestTouchTimeAnalysis(BestTouchTimeAnalysis data);

    List<BestTouchTimeAnalysis> selectList(BestTouchTimeAnalysis filter);
}