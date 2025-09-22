package com.omniperform.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.omniperform.web.mapper.GuideInfoMapper;
import com.omniperform.web.domain.GuideInfo;
import com.omniperform.web.service.IGuideInfoService;

/**
 * 导购基础信息Service业务层处理
 * 
 * @author omniperform
 * @date 2024-01-01
 */
@Service
public class GuideInfoServiceImpl implements IGuideInfoService 
{
    @Autowired
    private GuideInfoMapper guideInfoMapper;

    /**
     * 查询导购基础信息
     * 
     * @param guideId 导购基础信息主键
     * @return 导购基础信息
     */
    @Override
    public GuideInfo selectGuideInfoByGuideId(Long guideId)
    {
        return guideInfoMapper.selectGuideInfoByGuideId(guideId);
    }

    /**
     * 查询导购基础信息列表
     * 
     * @param guideInfo 导购基础信息
     * @return 导购基础信息
     */
    @Override
    public List<GuideInfo> selectGuideInfoList(GuideInfo guideInfo)
    {
        return guideInfoMapper.selectGuideInfoList(guideInfo);
    }

    /**
     * 新增导购基础信息
     * 
     * @param guideInfo 导购基础信息
     * @return 结果
     */
    @Override
    public int insertGuideInfo(GuideInfo guideInfo)
    {
        return guideInfoMapper.insertGuideInfo(guideInfo);
    }

    /**
     * 修改导购基础信息
     * 
     * @param guideInfo 导购基础信息
     * @return 结果
     */
    @Override
    public int updateGuideInfo(GuideInfo guideInfo)
    {
        return guideInfoMapper.updateGuideInfo(guideInfo);
    }

    /**
     * 批量删除导购基础信息
     * 
     * @param guideIds 需要删除的导购基础信息主键
     * @return 结果
     */
    @Override
    public int deleteGuideInfoByGuideIds(Long[] guideIds)
    {
        return guideInfoMapper.deleteGuideInfoByGuideIds(guideIds);
    }

    /**
     * 删除导购基础信息信息
     * 
     * @param guideId 导购基础信息主键
     * @return 结果
     */
    @Override
    public int deleteGuideInfoByGuideId(Long guideId)
    {
        return guideInfoMapper.deleteGuideInfoByGuideId(guideId);
    }

    /**
     * 根据导购编号查询导购信息
     * 
     * @param guideCode 导购编号
     * @return 导购信息
     */
    @Override
    public GuideInfo selectGuideInfoByGuideCode(String guideCode)
    {
        return guideInfoMapper.selectGuideInfoByGuideCode(guideCode);
    }

    /**
     * 根据门店ID查询导购列表
     * 
     * @param storeId 门店ID
     * @return 导购列表
     */
    @Override
    public List<GuideInfo> selectGuideInfoByStoreId(Long storeId)
    {
        return guideInfoMapper.selectGuideInfoByStoreId(storeId);
    }

    /**
     * 根据区域代码查询导购列表
     * 
     * @param regionCode 区域代码
     * @return 导购列表
     */
    @Override
    public List<GuideInfo> selectGuideInfoByRegionCode(String regionCode)
    {
        return guideInfoMapper.selectGuideInfoByRegionCode(regionCode);
    }

    /**
     * 查询在职导购列表
     * 
     * @return 在职导购列表
     */
    @Override
    public List<GuideInfo> selectActiveGuideInfoList()
    {
        return guideInfoMapper.selectActiveGuideInfoList();
    }
}