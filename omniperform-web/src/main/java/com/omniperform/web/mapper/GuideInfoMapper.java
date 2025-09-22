package com.omniperform.web.mapper;

import java.util.List;
import com.omniperform.web.domain.GuideInfo;

/**
 * 导购基础信息Mapper接口
 * 
 * @author omniperform
 * @date 2025-01-15
 */
public interface GuideInfoMapper 
{
    /**
     * 查询导购基础信息
     * 
     * @param guideId 导购基础信息主键
     * @return 导购基础信息
     */
    public GuideInfo selectGuideInfoByGuideId(Long guideId);

    /**
     * 查询导购基础信息列表
     * 
     * @param guideInfo 导购基础信息
     * @return 导购基础信息集合
     */
    public List<GuideInfo> selectGuideInfoList(GuideInfo guideInfo);

    /**
     * 新增导购基础信息
     * 
     * @param guideInfo 导购基础信息
     * @return 结果
     */
    public int insertGuideInfo(GuideInfo guideInfo);

    /**
     * 修改导购基础信息
     * 
     * @param guideInfo 导购基础信息
     * @return 结果
     */
    public int updateGuideInfo(GuideInfo guideInfo);

    /**
     * 删除导购基础信息
     * 
     * @param guideId 导购基础信息主键
     * @return 结果
     */
    public int deleteGuideInfoByGuideId(Long guideId);

    /**
     * 批量删除导购基础信息
     * 
     * @param guideIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGuideInfoByGuideIds(Long[] guideIds);

    /**
     * 根据导购编号查询导购信息
     * 
     * @param guideCode 导购编号
     * @return 导购基础信息
     */
    public GuideInfo selectGuideInfoByGuideCode(String guideCode);

    /**
     * 根据门店ID查询导购列表
     * 
     * @param storeId 门店ID
     * @return 导购基础信息集合
     */
    public List<GuideInfo> selectGuideInfoByStoreId(Long storeId);

    /**
     * 根据区域代码查询导购列表
     * 
     * @param regionCode 区域代码
     * @return 导购基础信息集合
     */
    public List<GuideInfo> selectGuideInfoByRegionCode(String regionCode);

    /**
     * 查询在职导购列表
     * 
     * @return 导购基础信息集合
     */
    public List<GuideInfo> selectActiveGuideInfoList();
}